package org.homepisec.control.core;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReadingsService {

    private static final int EVENTS_LIMIT = 60 * 60 * 24 * 356;
    private final Map<String, Device> devices = new ConcurrentHashMap<>();
    private final LinkedList<EnrichedEvent> events = new LinkedList<>();
    private final PublishSubject<EnrichedEvent> eventsSubject;

    @Autowired
    public ReadingsService(PublishSubject<EnrichedEvent> eventsSubject) {
        this.eventsSubject = eventsSubject;
    }

    public Device getDevice(String deviceId) {
        return devices.get(deviceId);
    }

    public void handleDeviceRead(List<DeviceReading> readings) {
        readings.forEach(reading -> {
            Device device = getDevice(reading);
            final EnrichedEvent ee = new EnrichedEvent<>(
                    EventType.DEVICE_READ,
                    new Date(),
                    device,
                    reading.getValue()
            );
            eventsSubject.onNext(ee);
            events.addFirst(ee);
            if (events.size() > EVENTS_LIMIT) {
                events.pollLast();
            }
        });
    }

    private Device getDevice(DeviceReading reading) {
        final Device device = reading.getDevice();
        final String deviceId = device.getId();
        Device d = devices.get(deviceId);
        if (d == null) {
            d = new Device(deviceId, device.getType());
            devices.put(deviceId, d);
        }
        return d;
    }

    public List<EnrichedEvent> readEvents(int offset, int count) {
        int lowerBound = events.size() < offset ? events.size() : offset;
        int upperBound = events.size() < offset + count ? events.size() : offset + count;
        return events.subList(lowerBound, upperBound);
    }

    public List<Device> getDevices() {
        return new ArrayList<>(devices.values());
    }
}

package org.homepisec.control;

import org.homepisec.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReadingsService {

    private final static int EVENTS_LIMIT = 100000;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, Device> devices = new ConcurrentHashMap<>();
    private final LinkedList<EnrichedEvent> events = new LinkedList<>();

    public Device getDevice(String deviceId) {
        return devices.get(deviceId);
    }

    public void handleDeviceRead(List<DeviceReading> readings) {
        readings.forEach(reading -> {
            Device d = getDevice(reading);
            EnrichedEvent ee = new EnrichedEvent(
                    EventType.DEVICE_READ,
                    new Date(),
                    d,
                    reading.getValue()
            );
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
            d = new Device(deviceId, DeviceType.UNKNOWN);
            devices.put(deviceId, d);
        }
        return d;
    }

    public List<EnrichedEvent> readEvents(int offset, int count) {
        int lowerBound = events.size() < offset ? events.size() : offset;
        int upperBound = events.size() < offset + count ? events.size() : offset + count;
        return events.subList(lowerBound, upperBound);
    }

    List<Device> getDevices() {
        return new ArrayList<>(devices.values());
    }
}

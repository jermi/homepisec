package org.homepisec.control.core;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceReading;
import org.homepisec.control.rest.dto.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReadingsService {

    private final int readingTtlSeconds;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PublishSubject<DeviceEvent> eventsSubject;
    private final ArrayDeque<DeviceEvent> readings = new ArrayDeque<>();

    public ReadingsService(
            @Value(("${readingTtlSeconds}"))
            int readingTtlSeconds,
            PublishSubject<DeviceEvent> eventsSubject
    ) {
        this.readingTtlSeconds = readingTtlSeconds;
        this.eventsSubject = eventsSubject;
    }

    public void emitDeviceReadEvent(final List<DeviceReading> readings) {
        readings.forEach(reading -> {
            final DeviceEvent ee = new DeviceEvent(
                    EventType.DEVICE_READ,
                    System.currentTimeMillis(),
                    reading.getDevice(),
                    reading.getValue()
            );
            addReading(ee);
            eventsSubject.onNext(ee);
        });
    }

    public List<DeviceEvent> getReadings() {
        return new ArrayList<>(readings);
    }

    public List<Device> getDevices() {
        synchronized (readings) {
            return readings
                    .stream()
                    .map(DeviceEvent::getDevice)
                    .collect(Collectors.toList());
        }
    }

    private void addReading(DeviceEvent event) {
        final String deviceId = event.getDevice().getId();
        synchronized (readings) {
            final Iterator<DeviceEvent> it = readings.descendingIterator();
            while (it.hasNext()) {
                final DeviceEvent entry = it.next();
                if (entry.getDevice().getId().equals(deviceId)) {
                    it.remove();
                    break;
                }
            }
            readings.add(event);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void removeOldReadings() {
        synchronized (readings) {
            final long ttlLimit = System.currentTimeMillis() + readingTtlSeconds * 1000;
            final Iterator<DeviceEvent> it = readings.descendingIterator();
            while (it.hasNext()) {
                final DeviceEvent entry = it.next();
                if (ttlLimit - entry.getTime() < 0) {
                    logger.debug("removing old reading entry {}", entry);
                    it.remove();
                }
            }
        }
    }

}

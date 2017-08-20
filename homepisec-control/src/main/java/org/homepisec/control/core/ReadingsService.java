package org.homepisec.control.core;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.dto.Device;
import org.homepisec.dto.DeviceReading;
import org.homepisec.dto.EnrichedEvent;
import org.homepisec.dto.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReadingsService {

    private static final int READING_TTL_SECONDS = 60;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PublishSubject<EnrichedEvent> eventsSubject;
    private final ArrayDeque<EnrichedEvent> readings = new ArrayDeque<>();

    @Autowired
    public ReadingsService(PublishSubject<EnrichedEvent> eventsSubject) {
        this.eventsSubject = eventsSubject;
    }

    public void handleDeviceRead(List<DeviceReading> readings) {
        readings.forEach(reading -> {
            final EnrichedEvent ee = new EnrichedEvent<>(
                    EventType.DEVICE_READ,
                    new Date(),
                    reading.getDevice(),
                    reading.getValue()
            );
            addReading(ee);
            eventsSubject.onNext(ee);
        });
    }

    public List<EnrichedEvent> getReadings() {
        return new ArrayList<>(readings);
    }

    public List<Device> getDevices() {
        synchronized (readings) {
            return readings
                    .stream()
                    .map(EnrichedEvent::getDevice)
                    .collect(Collectors.toList());
        }
    }

    private void addReading(EnrichedEvent event) {
        final String deviceId = event.getDevice().getId();
        synchronized (readings) {
            final Iterator<EnrichedEvent> it = readings.descendingIterator();
            while (it.hasNext()) {
                final EnrichedEvent entry = it.next();
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
            final Date limit = Date.from(LocalDateTime.now()
                    .minus(READING_TTL_SECONDS, ChronoUnit.SECONDS)
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
            final Iterator<EnrichedEvent> it = readings.descendingIterator();
            while (it.hasNext()) {
                final EnrichedEvent entry = it.next();
                if (entry.getTime().compareTo(limit) < 0) {
                    logger.debug("removing old reading entry {}", entry);
                    it.remove();
                }
            }
        }
    }

}

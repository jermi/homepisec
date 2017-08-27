package org.homepisec.control.core;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceType;
import org.homepisec.control.rest.dto.EventType;
import org.homepisec.control.rest.dto.SensorAppEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SensorAppRegistry {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final int readingTtlSeconds;
    private final Disposable disposable;
    private Map<String, EndpointHolder> endpoints = Collections.synchronizedMap(new HashMap<>());

    public SensorAppRegistry(
            @Value(("${readingTtlSeconds"))
            int readingTtlSeconds,
            PublishSubject<DeviceEvent> eventsSubject
    ) {
        this.readingTtlSeconds = readingTtlSeconds;
        disposable = eventsSubject.subscribe(this::handleDeviceRead);
    }

    @PreDestroy
    public void destroy() {
        disposable.dispose();
    }

    @Scheduled(fixedRate = 1000)
    public void removeOldReadings() {
        final Iterator<Map.Entry<String, EndpointHolder>> it = endpoints.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<String, EndpointHolder> entry = it.next();
            final EndpointHolder holder = entry.getValue();
            final LocalDateTime ttlTime = holder.date.plus(readingTtlSeconds, ChronoUnit.SECONDS);
            if (ttlTime.compareTo(LocalDateTime.now()) < 0) {
                logger.info("removing endpoint  {}", holder.endpoint.getUrl());
                it.remove();
            }
        }
    }

    private void handleDeviceRead(DeviceEvent<?> event) {
        if (EventType.DEVICE_READ.equals(event.getType()) && DeviceType.SENSOR_APP.equals(event.getDevice().getType())) {
            handleSensorAppEvent((DeviceEvent<SensorAppEndpoint>) event);
        }
    }

    private void handleSensorAppEvent(DeviceEvent<SensorAppEndpoint> event) {
        final SensorAppEndpoint endpoint = event.getPayload();
        final String endpointUrl = endpoint.getUrl();
        EndpointHolder endpointHolder = endpoints.get(endpointUrl);
        if (endpointHolder == null) {
            endpointHolder = new EndpointHolder(endpoint);
            logger.info("adding new endpoint {}", endpointUrl);
            endpoints.put(endpointUrl, endpointHolder);
        }
        endpointHolder.endpoint = endpoint;
        endpointHolder.date = LocalDateTime.now();
    }

    private static class EndpointHolder {
        SensorAppEndpoint endpoint;
        LocalDateTime date = LocalDateTime.now();

        private EndpointHolder(SensorAppEndpoint endpoint) {
            this.endpoint = endpoint;
        }
    }

    public List<SensorAppEndpoint> getEndpoints() {
        return endpoints.values()
                .stream()
                .map(h -> h.endpoint)
                .collect(Collectors.toList());
    }

}

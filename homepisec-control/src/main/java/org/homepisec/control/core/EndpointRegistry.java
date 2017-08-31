package org.homepisec.control.core;

import org.homepisec.control.rest.dto.SensorAppEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EndpointRegistry {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final int readingTtlSeconds;
    private Map<String, EndpointHolder> endpoints = Collections.synchronizedMap(new HashMap<>());

    public EndpointRegistry(
            @Value("${readingTtlSeconds}") int readingTtlSeconds
    ) {
        this.readingTtlSeconds = readingTtlSeconds;
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

    public void addOrUpdate(final SensorAppEndpoint endpoint) {
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

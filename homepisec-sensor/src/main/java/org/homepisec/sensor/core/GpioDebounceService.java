package org.homepisec.sensor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class GpioDebounceService {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    private final GpioProvider gpioProvider;
    private final Map<Integer, Long> pinTimestampMap = new ConcurrentHashMap<>();

    public GpioDebounceService(GpioProvider gpioProvider) {
        this.gpioProvider = gpioProvider;
    }

    public void registerDebounce(final DeviceRegistry.DeviceGpio relay) {
        final Integer debounceDelay = relay.getDebounceDelay();
        if (debounceDelay != null) {
            scheduledExecutorService.schedule(
                    new DebounceTask(gpioProvider, relay.getGpio(), relay.isDebounceValue()),
                    debounceDelay,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    private final class DebounceTask implements Runnable {

        private final Logger logger = LoggerFactory.getLogger(getClass());
        private final GpioProvider gpioProvider;
        private final int pin;
        private final boolean debounceValue;
        private final long timestamp;

        private DebounceTask(GpioProvider gpioProvider, int pin, boolean debounceValue) {
            this.gpioProvider = gpioProvider;
            this.pin = pin;
            this.debounceValue = debounceValue;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public void run() {
            final Long lastDebounceTimestamp = pinTimestampMap.get(pin);
            if (lastDebounceTimestamp == null || lastDebounceTimestamp <= timestamp) {
                gpioProvider.setPinDirection(pin, GpioProvider.Direction.OUT);
                if (gpioProvider.readPin(pin) != debounceValue) {
                    gpioProvider.writePin(pin, debounceValue);
                    pinTimestampMap.put(pin, timestamp);
                } else {
                    logger.debug("pin {} already set to {}, skipping", pin, debounceValue);
                }
            } else {
                logger.debug("debounce task with timestamp {} for pin {} not latest, skipping", timestamp, pin);
            }
        }
    }

}

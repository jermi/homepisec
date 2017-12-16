package org.homepisec.sensor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RelayService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DeviceRegistryProvider deviceRegistryProvider;
    private final GpioProvider gpioProvider;
    private final GpioDebounceService gpioDebounceService;

    public RelayService(DeviceRegistryProvider deviceRegistryProvider, GpioProvider gpioProvider, GpioDebounceService gpioDebounceService) {
        this.deviceRegistryProvider = deviceRegistryProvider;
        this.gpioProvider = gpioProvider;
        this.gpioDebounceService = gpioDebounceService;
    }

    public List<DeviceRegistry.DeviceGpio> getAllRelays() {
        final DeviceRegistry deviceRegistry = deviceRegistryProvider.loadRegistry();
        final List<DeviceRegistry.DeviceGpio> alarmRelays = deviceRegistry.getAlarmRelays();
        final List<DeviceRegistry.DeviceGpio> relays = deviceRegistry.getRelays();
        final List<DeviceRegistry.DeviceGpio> allRelays = new ArrayList<>(alarmRelays.size() + relays.size());
        allRelays.addAll(alarmRelays);
        allRelays.addAll(relays);
        return allRelays;
    }

    public boolean switchRelay(final String relayId, final boolean value) {
        logger.debug("switching relay {} to {}", relayId, value);
        return getAllRelays()
                .stream()
                .filter(r -> r.getId().equals(relayId))
                .findFirst()
                .map(r -> {
                    switchRelayGpio(r, value);
                    return true;
                })
                .orElse(false);
    }

    private void switchRelayGpio(final DeviceRegistry.DeviceGpio relay, final boolean value) {
        final int pin = relay.getGpio();
        gpioProvider.setPinDirection(pin, GpioProvider.Direction.OUT);
        gpioProvider.writePin(pin, value);
        final Integer delay = relay.getDebounceDelay();
        final boolean debounceValueDifferent = value != relay.isDebounceValue();
        if(delay != null && delay > 0 && debounceValueDifferent) {
            gpioDebounceService.registerDebounce(relay);
        }
    }

}

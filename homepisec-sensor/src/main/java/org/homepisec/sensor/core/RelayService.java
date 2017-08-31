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

    public RelayService(DeviceRegistryProvider deviceRegistryProvider, GpioProvider gpioProvider) {
        this.deviceRegistryProvider = deviceRegistryProvider;
        this.gpioProvider = gpioProvider;
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

    public boolean switchRelay(DeviceRegistry.DeviceGpio relay, boolean value) {
        logger.debug("switching relay {} to {}", relay, value);
        final int pin = relay.getGpio();
        gpioProvider.setPinDirection(pin, GpioProvider.Direction.OUT);
        gpioProvider.writePin(pin, value);
        return true;
    }

}

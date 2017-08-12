package org.homepisec.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RelayService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CapabilitiesProvider capabilitiesProvider;
    private final GpioProvider gpioProvider;

    public RelayService(CapabilitiesProvider capabilitiesProvider, GpioProvider gpioProvider) {
        this.capabilitiesProvider = capabilitiesProvider;
        this.gpioProvider = gpioProvider;
    }

    public List<Capabilities.DeviceGpio> getAllRelays() {
        final Capabilities capabilities = capabilitiesProvider.loadCapabilities();
        final List<Capabilities.DeviceGpio> alarmRelays = capabilities.getAlarmRelays();
        final List<Capabilities.DeviceGpio> relays = capabilities.getRelays();
        final List<Capabilities.DeviceGpio> allRelays = new ArrayList<>(alarmRelays.size() + relays.size());
        allRelays.addAll(alarmRelays);
        allRelays.addAll(relays);
        return allRelays;
    }

    public boolean switchRelay(Capabilities.DeviceGpio relay, Boolean value) {
        logger.debug("switching relay {} to {}", relay, value);
        final int pin = relay.getGpio();
        gpioProvider.enablePin(pin);
        gpioProvider.setPinDirection(pin, GpioProvider.Direction.OUT);
        gpioProvider.writePin(pin, value);
        return true;
    }

}

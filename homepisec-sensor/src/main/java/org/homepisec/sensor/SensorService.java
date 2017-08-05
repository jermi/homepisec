package org.homepisec.sensor;

import org.homepisec.dto.Device;
import org.homepisec.dto.DeviceReading;
import org.homepisec.dto.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SensorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ControlApiRestClient controlApiRestClient;
    private final GpioProvider gpioProvider;
    private final CapabilitiesProvider capabilitiesProvider;

    @Autowired
    public SensorService(
            ControlApiRestClient controlApiRestClient,
            GpioProvider gpioProvider,
            CapabilitiesProvider capabilitiesProvider
    ) {
        this.controlApiRestClient = controlApiRestClient;
        this.gpioProvider = gpioProvider;
        this.capabilitiesProvider = capabilitiesProvider;
    }

    @Scheduled(fixedRateString = "${updateFrequency}")
    public void sendDataToControl() {
        final Capabilities capabilities = capabilitiesProvider.loadCapabilities();
        final List<DeviceReading> readings = new ArrayList<>();
        populateMotionSensorReadings(readings, capabilities.getMotionSensors());
        logger.debug("sending readings {} to control", readings);
        controlApiRestClient.postReadings(readings);
    }

    private void populateMotionSensorReadings(List<DeviceReading> readings, List<Capabilities.DeviceGpio> motionSensors) {
        for (Capabilities.DeviceGpio deviceGpio : motionSensors) {
            final int pin = deviceGpio.getGpio();
            final boolean value = gpioProvider.readPin(pin);
            final Device device = new Device(deviceGpio.getId(), DeviceType.SENSOR_MOTION);
            final DeviceReading deviceReading = new DeviceReading(device, String.valueOf(value));
            readings.add(deviceReading);
        }
    }

}

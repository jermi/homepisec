package org.homepisec.sensor.core;

import org.homepisec.dto.Device;
import org.homepisec.dto.DeviceReading;
import org.homepisec.dto.DeviceType;
import org.homepisec.sensor.rest.ControlApiRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SensorReadingsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ControlApiRestClient controlApiRestClient;
    private final GpioProvider gpioProvider;
    private final DeviceRegistryProvider deviceRegistryProvider;

    @Autowired
    public SensorReadingsService(
            ControlApiRestClient controlApiRestClient,
            GpioProvider gpioProvider,
            DeviceRegistryProvider deviceRegistryProvider
    ) {
        this.controlApiRestClient = controlApiRestClient;
        this.gpioProvider = gpioProvider;
        this.deviceRegistryProvider = deviceRegistryProvider;
    }

    @Scheduled(fixedRateString = "${updateFrequency}")
    public void sendDataToControl() {
        final DeviceRegistry deviceRegistry = deviceRegistryProvider.loadCapabilities();
        final List<DeviceReading> readings = new ArrayList<>();
        populateGpioPinReadings(readings, deviceRegistry.getMotionSensors(), DeviceType.SENSOR_MOTION);
        populateGpioPinReadings(readings, deviceRegistry.getRelays(), DeviceType.RELAY);
        populateGpioPinReadings(readings, deviceRegistry.getAlarmRelays(), DeviceType.RELAY);
        logger.debug("sending readings {} to control", readings);
        controlApiRestClient.postReadings(readings);
    }

    private void populateGpioPinReadings(List<DeviceReading> readings, List<DeviceRegistry.DeviceGpio> devices, DeviceType deviceType) {
        for (DeviceRegistry.DeviceGpio deviceGpio : devices) {
            final int pin = deviceGpio.getGpio();
            final boolean value = gpioProvider.readPin(pin);
            final Device device = new Device(deviceGpio.getId(), deviceType);
            final DeviceReading deviceReading = new DeviceReading(device, String.valueOf(value));
            readings.add(deviceReading);
        }
    }



}

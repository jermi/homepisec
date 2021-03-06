package org.homepisec.sensor.core;

import org.homepisec.sensor.rest.client.api.ReadingsControllerApi;
import org.homepisec.sensor.rest.client.model.Device;
import org.homepisec.sensor.rest.client.model.DeviceReading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SensorReadingsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ReadingsControllerApi readingsControllerApi;
    private final GpioProvider gpioProvider;
    private final DeviceRegistryProvider deviceRegistryProvider;
    private final W1TempReader w1TempReader;

    public SensorReadingsService(
            ReadingsControllerApi readingsControllerApi,
            GpioProvider gpioProvider,
            DeviceRegistryProvider deviceRegistryProvider,
            W1TempReader w1TempReader) {
        this.readingsControllerApi = readingsControllerApi;
        this.gpioProvider = gpioProvider;
        this.deviceRegistryProvider = deviceRegistryProvider;
        this.w1TempReader = w1TempReader;
    }

    @Scheduled(fixedRateString = "${updateFrequency}")
    public void sendDataToControl() {
        final DeviceRegistry deviceRegistry = deviceRegistryProvider.loadRegistry();
        final List<DeviceReading> readings = new ArrayList<>();
        populateGpioPinReadings(readings, deviceRegistry.getMotionSensors(), Device.TypeEnum.SENSOR_MOTION);
        populateGpioPinReadings(readings, deviceRegistry.getRelays(), Device.TypeEnum.RELAY);
        populateGpioPinReadings(readings, deviceRegistry.getAlarmRelays(), Device.TypeEnum.RELAY);
        readings.addAll(w1TempReader.getTempReadings(deviceRegistry.getTempSensors()));
        logger.debug("sending readings {} to control", readings);
        readingsControllerApi.postReadingsUsingPOST(readings);
    }

    private void populateGpioPinReadings(List<DeviceReading> readings, List<DeviceRegistry.DeviceGpio> devices, Device.TypeEnum deviceType) {
        for (DeviceRegistry.DeviceGpio deviceGpio : devices) {
            final int pin = deviceGpio.getGpio();
            final boolean pinValue = gpioProvider.readPin(pin);
            final DeviceRegistry.ContactType contactType = deviceGpio.getContactType();
            final boolean normalizedPinValue;
            if (DeviceRegistry.ContactType.NORMALLY_CLOSED.equals(contactType)) {
                // for NC need to inverse pin raw reading since
                // high pin state = low logical state and low pin state = high logical state
                normalizedPinValue = !pinValue;
            } else {
                // for NO use raw pin value since pin state = logical state
                normalizedPinValue = pinValue;
            }
            final Device device = new Device().id(deviceGpio.getId()).type(deviceType);
            final DeviceReading deviceReading = new DeviceReading().device(device).value(String.valueOf(normalizedPinValue));
            readings.add(deviceReading);
        }
    }

}

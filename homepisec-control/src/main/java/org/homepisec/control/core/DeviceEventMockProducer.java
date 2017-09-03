package org.homepisec.control.core;

import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceReading;
import org.homepisec.control.rest.dto.DeviceType;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Profile("mock")
public class DeviceEventMockProducer {

    private final ReadingsService readingsService;
    private final List<Device> devices;
    private final Map<String, BigDecimal> tempValues = new HashMap<>();

    public DeviceEventMockProducer(ReadingsService readingsService) {
        this.readingsService = readingsService;
        devices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final Device d = new Device(
                    "mocked-device-" + i,
                    i % 2 == 0 ? DeviceType.SENSOR_MOTION : DeviceType.SENSOR_TEMP
            );
            devices.add(d);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void produceFakeReadings() {
        readingsService.emitDeviceReadEvent(generateReadings());
    }

    private List<DeviceReading> generateReadings() {
        return devices.stream().map(device ->
                new DeviceReading(device, genereratePayload(device))
        ).collect(Collectors.toList());
    }

    private String genereratePayload(Device device) {
        final String payload;
        switch (device.getType()) {
            case SENSOR_TEMP:
                BigDecimal prevVal = tempValues.get(device.getId());
                if (prevVal == null) {
                    prevVal = new BigDecimal("20").setScale(3, BigDecimal.ROUND_HALF_UP);
                }
                final BigDecimal delta = Math.random() > 0.5 ? BigDecimal.ONE : BigDecimal.ZERO;
                final BigDecimal newVal;
                if (Math.random() > 0.5) {
                    newVal = prevVal.add(delta);
                } else {
                    newVal = prevVal.subtract(delta);
                }
                tempValues.put(device.getId(), newVal);
                payload = newVal.toString();
                break;
            default:
                payload = String.valueOf(Math.random() > 0.5);
                break;
        }
        return payload;
    }

}

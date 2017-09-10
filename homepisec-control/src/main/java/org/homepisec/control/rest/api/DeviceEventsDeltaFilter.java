package org.homepisec.control.rest.api;

import io.reactivex.functions.Predicate;
import org.homepisec.control.rest.dto.DeviceEvent;

import java.util.HashMap;
import java.util.Map;

public class DeviceEventsDeltaFilter implements Predicate<DeviceEvent> {

    private final Map<String, DeviceEvent> readings = new HashMap<>();

    @Override
    public boolean test(final DeviceEvent newReading) {
        final String deviceId = newReading.getDevice().getId();
        final DeviceEvent oldReading = readings.get(deviceId);
        if (oldReading == null || isPayloadChanged(newReading, oldReading)) {
            readings.put(deviceId, newReading);
            return true;
        }
        return false;
    }

    private static boolean isPayloadChanged(
            final DeviceEvent newReading,
            final DeviceEvent oldReading
    ) {
        return !oldReading.getPayload().equals(newReading.getPayload());
    }

}

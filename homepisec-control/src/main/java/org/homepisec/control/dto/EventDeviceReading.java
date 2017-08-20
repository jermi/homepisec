package org.homepisec.control.dto;

import java.util.List;

public class EventDeviceReading extends Event<List<DeviceReading>> {

    private EventDeviceReading() {
        super(null, null);
    }

    public EventDeviceReading(List<DeviceReading> deviceReadings) {
        super(EventType.DEVICE_READ, deviceReadings);
    }
}

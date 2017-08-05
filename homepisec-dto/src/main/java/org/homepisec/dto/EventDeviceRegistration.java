package org.homepisec.dto;

import java.util.List;

public class EventDeviceRegistration extends Event<List<Device>>{
    private EventDeviceRegistration() {
        super(null, null);
    }

    public EventDeviceRegistration(List<Device> devices) {
        super(EventType.DEVICE_REGISTRATION, devices);
    }
}

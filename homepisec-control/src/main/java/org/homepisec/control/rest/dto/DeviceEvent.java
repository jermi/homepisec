package org.homepisec.control.rest.dto;

import java.util.Date;

public class DeviceEvent extends Event {

    private Date time;
    private Device device;

    public DeviceEvent() {
        super(null, null);
    }

    public DeviceEvent(EventType type, Date time, Device device, String payload) {
        super(type, payload);
        this.time = time;
        this.device = device;
    }

    public Date getTime() {
        return time;
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "DeviceEvent{" +
                "time=" + time +
                ", device=" + device +
                '}';
    }

}

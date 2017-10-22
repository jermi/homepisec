package org.homepisec.control.rest.dto;

public class DeviceEvent extends Event {

    private long time;
    private Device device;

    public DeviceEvent() {
        super(null, null);
    }

    public DeviceEvent(EventType type, long time, Device device, String payload) {
        super(type, payload);
        this.time = time;
        this.device = device;
    }

    public long getTime() {
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

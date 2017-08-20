package org.homepisec.control.dto;

public class Device {

    public static final Device CONTROL = new Device("CONTROL", DeviceType.CONTROL);

    private String id;
    private DeviceType type;

    private Device() {
    }

    public Device(String id, DeviceType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public DeviceType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", type=" + type +
                '}';
    }
}

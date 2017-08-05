package org.homepisec.dto;

public class Device {

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

}

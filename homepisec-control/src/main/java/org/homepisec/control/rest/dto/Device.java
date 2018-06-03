package org.homepisec.control.rest.dto;

import io.swagger.annotations.ApiModelProperty;

public class Device {

    public static final Device CONTROL = new Device(DeviceType.CONTROL.name(), DeviceType.CONTROL);

    @ApiModelProperty(required = true)
    private String id;
    @ApiModelProperty(required = true)
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

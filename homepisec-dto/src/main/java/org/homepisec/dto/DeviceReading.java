package org.homepisec.dto;

public class DeviceReading {

    private Device device;
    private String value;

    private DeviceReading() {
    }

    public DeviceReading(Device device, String value) {
        this.device = device;
        this.value = value;
    }

    public Device getDevice() {
        return device;
    }

    public String getValue() {
        return value;
    }

}

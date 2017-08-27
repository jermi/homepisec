package org.homepisec.control.rest.dto;

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

    @Override
    public String toString() {
        return "DeviceReading{" +
                "device=" + device +
                ", value='" + value + '\'' +
                '}';
    }
}

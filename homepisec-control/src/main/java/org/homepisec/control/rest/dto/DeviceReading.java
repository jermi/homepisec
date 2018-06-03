package org.homepisec.control.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

public class DeviceReading {

    @NotNull
    @ApiModelProperty(required = true)
    private Device device;
    @NotNull
    @ApiModelProperty(required = true)
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

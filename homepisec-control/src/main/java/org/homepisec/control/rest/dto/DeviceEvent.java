package org.homepisec.control.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

public class DeviceEvent extends Event {

    @NotNull
    @ApiModelProperty(required = true)
    private long time;
    @NotNull
    @ApiModelProperty(required = true)
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

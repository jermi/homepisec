package org.homepisec.control.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;

public class SensorAppEndpoint {

    @NotNull
    @ApiModelProperty(required = true)
    private String url;
    @NotNull
    @ApiModelProperty(required = true)
    private List<Device> relays;
    @NotNull
    @ApiModelProperty(required = true)
    private List<Device> alarmRelays;

    private SensorAppEndpoint() {
    }

    public SensorAppEndpoint(String url, List<Device> relays, List<Device> alarmRelays) {
        this.url = url;
        this.relays = relays;
        this.alarmRelays = alarmRelays;
    }

    public String getUrl() {
        return url;
    }

    public List<Device> getRelays() {
        return relays;
    }

    public List<Device> getAlarmRelays() {
        return alarmRelays;
    }

    public void setAlarmRelays(List<Device> alarmRelays) {
        this.alarmRelays = alarmRelays;
    }

    @Override
    public String toString() {
        return "SensorAppEndpoint{" +
                "url='" + url + '\'' +
                ", relays=" + relays +
                ", alarmRelays=" + alarmRelays +
                '}';
    }

}

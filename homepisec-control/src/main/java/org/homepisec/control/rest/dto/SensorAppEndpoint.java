package org.homepisec.control.rest.dto;

import java.util.List;

public class SensorAppEndpoint {

    private String url;
    private List<Device> relays;
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

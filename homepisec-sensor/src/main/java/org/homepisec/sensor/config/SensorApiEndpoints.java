package org.homepisec.sensor.config;

public final class SensorApiEndpoints {

    public static final String API = "/api";
    public static final String READINGS = "/readings";
    public static final String RELAYS = "/relays";
    public static final String ALARM = "/alarm";
    public static final String ALARM_ARM = ALARM + "/arm";
    public static final String ALARM_DISARM = ALARM + "/disarm";

    private SensorApiEndpoints() {
    }

}

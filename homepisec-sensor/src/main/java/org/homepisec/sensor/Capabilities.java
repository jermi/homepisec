package org.homepisec.sensor;

import java.util.List;

public class Capabilities {

    private List<DeviceGpio> motionSensors;
    private List<DeviceGpio> alarmRelays;
    private List<DeviceGpio> relays;

    /**
     * default constructor for Jackson
     */
    private Capabilities() {
    }

    public Capabilities(List<DeviceGpio> motionSensors) {
        this.motionSensors = motionSensors;
    }

    public List<DeviceGpio> getMotionSensors() {
        return motionSensors;
    }

    public List<DeviceGpio> getAlarmRelays() {
        return alarmRelays;
    }

    public List<DeviceGpio> getRelays() {
        return relays;
    }

    public static class DeviceGpio {
        private String id;
        private int gpio;

        private DeviceGpio() {
        }

        public DeviceGpio(String id, int gpio) {
            this.id = id;
            this.gpio = gpio;
        }

        public String getId() {
            return id;
        }

        public int getGpio() {
            return gpio;
        }
    }

}

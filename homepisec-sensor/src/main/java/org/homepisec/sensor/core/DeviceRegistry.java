package org.homepisec.sensor.core;

import java.util.List;
import java.util.Objects;

public class DeviceRegistry {

    private List<DeviceGpio> motionSensors;
    private List<DeviceGpio> alarmRelays;
    private List<DeviceGpio> relays;
    private List<DeviceW1> tempSensors;

    /**
     * default constructor for Jackson
     */
    private DeviceRegistry() {
    }

    public DeviceRegistry(List<DeviceGpio> motionSensors) {
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

    public List<DeviceW1> getTempSensors() {
        return tempSensors;
    }

    public enum ContactType {
        NORMALLY_CLOSED,
        NORMALLY_OPEN
    }

    public static class DeviceGpio {
        private String id;
        private int gpio;
        private Integer debounceDelay;
        private boolean debounceValue;
        private ContactType contactType;

        private DeviceGpio() {
        }

        public DeviceGpio(String id, int gpio, Integer debounceDelay, boolean debounceValue) {
            this.id = id;
            this.gpio = gpio;
            this.debounceDelay = debounceDelay;
            this.debounceValue = debounceValue;
        }

        public DeviceGpio(String id, int gpio, Integer debounceDelay, boolean debounceValue, ContactType contactType) {
            this.id = id;
            this.gpio = gpio;
            this.debounceDelay = debounceDelay;
            this.debounceValue = debounceValue;
            this.contactType = contactType;
        }

        public String getId() {
            return id;
        }

        public int getGpio() {
            return gpio;
        }

        public Integer getDebounceDelay() {
            return debounceDelay;
        }

        public boolean isDebounceValue() {
            return debounceValue;
        }

        public ContactType getContactType() {
            return contactType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DeviceGpio that = (DeviceGpio) o;
            return gpio == that.gpio &&
                    debounceValue == that.debounceValue &&
                    Objects.equals(id, that.id) &&
                    Objects.equals(debounceDelay, that.debounceDelay) &&
                    contactType == that.contactType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, gpio, debounceDelay, debounceValue, contactType);
        }

        @Override
        public String toString() {
            return "DeviceGpio{" +
                    "id='" + id + '\'' +
                    ", gpio=" + gpio +
                    ", debounceDelay=" + debounceDelay +
                    ", debounceValue=" + debounceValue +
                    ", contactType=" + contactType +
                    '}';
        }
    }

    public static class DeviceW1 {
        private String id;
        private String w1SerialCode;

        private DeviceW1() {
        }

        public DeviceW1(String id, String w1SerialCode) {
            this.id = id;
            this.w1SerialCode = w1SerialCode;
        }

        public String getId() {
            return id;
        }

        public String getW1SerialCode() {
            return w1SerialCode;
        }

        @Override
        public String toString() {
            return "DeviceW1{" +
                    "id='" + id + '\'' +
                    ", w1SerialCode='" + w1SerialCode + '\'' +
                    '}';
        }
    }

}

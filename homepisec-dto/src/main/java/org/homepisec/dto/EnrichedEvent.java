package org.homepisec.dto;

import java.util.Date;

public class EnrichedEvent {

    private Date time;
    private Device device;
    private EventType type;
    private String value;

    private EnrichedEvent() {
    }

    public EnrichedEvent(EventType type, Date time, Device device, String value) {
        this.time = time;
        this.device = device;
        this.value = value;
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public Device getDevice() {
        return device;
    }

    public EventType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

}

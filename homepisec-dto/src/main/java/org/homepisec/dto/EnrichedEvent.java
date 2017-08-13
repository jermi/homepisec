package org.homepisec.dto;

import java.util.Date;

public class EnrichedEvent<PAYLOAD> extends Event<PAYLOAD> {

    private Date time;
    private Device device;

    public EnrichedEvent() {
        super(null, null);
    }

    public EnrichedEvent(EventType type, Date time, Device device, PAYLOAD payload) {
        super(type, payload);
        this.time = time;
        this.device = device;
    }

    public Date getTime() {
        return time;
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "EnrichedEvent{" +
                "time=" + time +
                ", device=" + device +
                '}';
    }

}

package org.homepisec.control.rest.dto;

public class Event {

    private EventType type;
    private String payload;

    private Event() {
    }

    public Event(EventType type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public EventType getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

}

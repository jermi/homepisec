package org.homepisec.control.rest.dto;

public class Event<PAYLOAD> {

    private EventType type;
    private PAYLOAD payload;

    private Event() {
    }

    public Event(EventType type, PAYLOAD payload) {
        this.type = type;
        this.payload = payload;
    }

    public EventType getType() {
        return type;
    }

    public PAYLOAD getPayload() {
        return payload;
    }

}

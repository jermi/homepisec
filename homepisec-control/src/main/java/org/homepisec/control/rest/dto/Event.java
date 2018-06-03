package org.homepisec.control.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

public class Event {

    @NotNull
    @ApiModelProperty(required = true)
    private EventType type;
    @NotNull
    @ApiModelProperty(required = true)
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

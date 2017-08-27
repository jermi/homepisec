package org.homepisec.sensor.rest.dto;

import javax.validation.constraints.NotNull;

public class SwitchRelayRequest {

    @NotNull
    private String id;
    @NotNull
    private Boolean value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SwitchRelayRequest{" +
                "id='" + id + '\'' +
                ", value=" + value +
                '}';
    }
}

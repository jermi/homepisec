package org.homepisec.control.core.alarm;

import org.homepisec.control.rest.dto.Device;

import java.util.Date;

public class AlarmStatus {

    private AlarmState state = AlarmState.DISARMED;
    private Long countdownStart;
    private Long countdownEnd;
    private Device countdownSource;
    private Long triggerStart;
    private Device triggerSource;

    public AlarmState getState() {
        return state;
    }

    public void setState(AlarmState state) {
        this.state = state;
    }

    public Long getCountdownStart() {
        return countdownStart;
    }

    public void setCountdownStart(Long countdownStart) {
        this.countdownStart = countdownStart;
    }

    public Long getCountdownEnd() {
        return countdownEnd;
    }

    public void setCountdownEnd(Long countdownEnd) {
        this.countdownEnd = countdownEnd;
    }

    public Long getTriggerStart() {
        return triggerStart;
    }

    public void setTriggerStart(Long triggerStart) {
        this.triggerStart = triggerStart;
    }

    public Device getCountdownSource() {
        return countdownSource;
    }

    public void setCountdownSource(Device countdownSource) {
        this.countdownSource = countdownSource;
    }

    public Device getTriggerSource() {
        return triggerSource;
    }

    public void setTriggerSource(Device triggerSource) {
        this.triggerSource = triggerSource;
    }

    @Override
    public String toString() {
        return "AlarmStatus{" +
                "state=" + state +
                ", countdownStart=" + countdownStart +
                ", countdownEnd=" + countdownEnd +
                ", countdownSource=" + countdownSource +
                ", triggerStart=" + triggerStart +
                ", triggerSource=" + triggerSource +
                '}';
    }

}

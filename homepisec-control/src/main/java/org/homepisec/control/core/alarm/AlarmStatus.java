package org.homepisec.control.core.alarm;

import org.homepisec.control.rest.dto.Device;

import java.util.Date;

public class AlarmStatus {

    private AlarmState state = AlarmState.DISARMED;
    private Date countdownStart;
    private Date countdownEnd;
    private Device countdownSource;
    private Date triggerStart;
    private Device triggerSource;

    public AlarmState getState() {
        return state;
    }

    public void setState(AlarmState state) {
        this.state = state;
    }

    public Date getCountdownStart() {
        return countdownStart;
    }

    public void setCountdownStart(Date countdownStart) {
        this.countdownStart = countdownStart;
    }

    public Date getCountdownEnd() {
        return countdownEnd;
    }

    public void setCountdownEnd(Date countdownEnd) {
        this.countdownEnd = countdownEnd;
    }

    public Date getTriggerStart() {
        return triggerStart;
    }

    public void setTriggerStart(Date triggerStart) {
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

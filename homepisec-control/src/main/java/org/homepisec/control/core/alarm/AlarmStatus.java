package org.homepisec.control.core.alarm;

import java.util.Date;

public class AlarmStatus {

    private AlarmState state = AlarmState.DISARMED;
    private Date countdownStart;
    private Date countdownEnd;
    private Date triggerStart;

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

    @Override
    public String toString() {
        return "AlarmStatus{" +
                "state=" + state +
                ", countdownStart=" + countdownStart +
                ", countdownEnd=" + countdownEnd +
                ", triggerStart=" + triggerStart +
                '}';
    }
}

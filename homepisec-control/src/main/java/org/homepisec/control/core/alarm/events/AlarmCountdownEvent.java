package org.homepisec.control.core.alarm.events;

import org.homepisec.control.dto.Device;
import org.homepisec.control.dto.EnrichedEvent;
import org.homepisec.control.dto.EventType;

import java.util.Date;

public class AlarmCountdownEvent extends EnrichedEvent<Object> {
    public AlarmCountdownEvent(Date date) {
        super(EventType.ALARM_COUNTDOWN, date, Device.CONTROL, null);
    }
}

package org.homepisec.control.alarm.events;

import org.homepisec.dto.Device;
import org.homepisec.dto.EnrichedEvent;
import org.homepisec.dto.Event;
import org.homepisec.dto.EventType;

import java.util.Date;

public class AlarmCountdownEvent extends EnrichedEvent<Object> {
    public AlarmCountdownEvent(Date date) {
        super(EventType.ALARM_COUNTDOWN, date, Device.CONTROL, null);
    }
}

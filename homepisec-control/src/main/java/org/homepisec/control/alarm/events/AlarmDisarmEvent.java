package org.homepisec.control.alarm.events;

import org.homepisec.dto.Device;
import org.homepisec.dto.EnrichedEvent;
import org.homepisec.dto.EventType;

import java.util.Date;

public class AlarmDisarmEvent extends EnrichedEvent<Object> {
    public AlarmDisarmEvent(Date date) {
        super(EventType.ALARM_DISARM, date, Device.CONTROL, null);
    }
}

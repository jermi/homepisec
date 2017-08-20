package org.homepisec.control.core.alarm.events;

import org.homepisec.control.dto.Device;
import org.homepisec.control.dto.EnrichedEvent;
import org.homepisec.control.dto.EventType;

import java.util.Date;

public class AlarmDisarmEvent extends EnrichedEvent<Object> {
    public AlarmDisarmEvent(Date date) {
        super(EventType.ALARM_DISARM, date, Device.CONTROL, null);
    }
}

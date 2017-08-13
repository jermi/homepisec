package org.homepisec.control.core.alarm.events;

import org.homepisec.dto.Device;
import org.homepisec.dto.EnrichedEvent;
import org.homepisec.dto.EventType;

import java.util.Date;

public class AlarmTriggeredEvent extends EnrichedEvent<Object> {

    public AlarmTriggeredEvent(Date date) {
        super(EventType.ALARM_TRIGGER, date, Device.CONTROL, null);
    }

}

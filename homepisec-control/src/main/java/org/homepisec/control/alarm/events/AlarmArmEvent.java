package org.homepisec.control.alarm.events;

import org.homepisec.dto.Device;
import org.homepisec.dto.EnrichedEvent;
import org.homepisec.dto.EventType;

import java.util.Date;

public class AlarmArmEvent extends EnrichedEvent<Object> {
    public AlarmArmEvent(Date date) {
        super(EventType.ALARM_ARM, date, Device.CONTROL, null);
    }
}

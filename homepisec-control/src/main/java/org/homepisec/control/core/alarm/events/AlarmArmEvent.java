package org.homepisec.control.core.alarm.events;

import org.homepisec.control.dto.Device;
import org.homepisec.control.dto.EnrichedEvent;
import org.homepisec.control.dto.EventType;

import java.util.Date;

public class AlarmArmEvent extends EnrichedEvent<Object> {
    public AlarmArmEvent(Date date) {
        super(EventType.ALARM_ARM, date, Device.CONTROL, null);
    }
}

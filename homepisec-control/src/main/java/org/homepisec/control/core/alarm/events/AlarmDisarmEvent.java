package org.homepisec.control.core.alarm.events;

import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.EventType;

import java.util.Date;

public class AlarmDisarmEvent extends DeviceEvent<Object> {
    public AlarmDisarmEvent(Date date) {
        super(EventType.ALARM_DISARM, date, Device.CONTROL, null);
    }
}

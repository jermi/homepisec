package org.homepisec.control.core.alarm.events;

import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.EventType;

import java.util.Date;

public class AlarmCountdownEvent extends DeviceEvent {
    public AlarmCountdownEvent(Date date, String deviceIdTrigger) {
        super(EventType.ALARM_COUNTDOWN, date, Device.CONTROL, deviceIdTrigger);
    }
}

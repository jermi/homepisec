package org.homepisec.control.core.alarm.events;

import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.EventType;

import java.util.Date;

public class AlarmCountdownEvent extends DeviceEvent {
    public AlarmCountdownEvent(long time, String deviceIdTrigger) {
        super(EventType.ALARM_COUNTDOWN, time, Device.CONTROL, deviceIdTrigger);
    }
}

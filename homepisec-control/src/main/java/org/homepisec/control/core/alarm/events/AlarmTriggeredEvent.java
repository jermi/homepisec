package org.homepisec.control.core.alarm.events;

import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.EventType;

import java.util.Date;

public class AlarmTriggeredEvent extends DeviceEvent {

    public AlarmTriggeredEvent(long time, String deviceIdTrigger) {
        super(EventType.ALARM_TRIGGER, time, Device.CONTROL, deviceIdTrigger);
    }

}

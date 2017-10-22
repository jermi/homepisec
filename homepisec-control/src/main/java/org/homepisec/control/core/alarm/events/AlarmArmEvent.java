package org.homepisec.control.core.alarm.events;

import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.EventType;

import java.util.Date;

public class AlarmArmEvent extends DeviceEvent {
    public AlarmArmEvent(long time) {
        super(EventType.ALARM_ARM, time, Device.CONTROL, null);
    }
}

package org.homepisec.control.core.alarm.events;

import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.EventType;

public class AlarmDisarmEvent extends DeviceEvent {
    public AlarmDisarmEvent(long time) {
        super(EventType.ALARM_DISARM, time, Device.CONTROL, null);
    }
}

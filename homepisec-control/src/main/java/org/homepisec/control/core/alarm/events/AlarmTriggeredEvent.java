package org.homepisec.control.core.alarm.events;

import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.EventType;

public class AlarmTriggeredEvent extends DeviceEvent {

    private Device source;

    public AlarmTriggeredEvent(long time, Device source) {
        super(EventType.ALARM_TRIGGER, time, Device.CONTROL, source.getId());
        this.source = source;
    }

    public Device getSource() {
        return source;
    }

}

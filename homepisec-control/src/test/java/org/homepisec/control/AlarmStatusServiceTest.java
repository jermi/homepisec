package org.homepisec.control;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.core.AlarmStatusService;
import org.homepisec.control.core.alarm.AlarmState;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceType;
import org.homepisec.control.rest.dto.EventType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class AlarmStatusServiceTest {

    @Test
    public void arm_and_disarm() {
        // given
        final PublishSubject<DeviceEvent> subject = PublishSubject.create();
        AlarmStatusService instance = new AlarmStatusService(-1, subject);
        Assert.assertEquals(AlarmState.DISARMED, instance.getAlarmStatus().getState());
        // when
        instance.armAlarm();
        // then
        Assert.assertEquals(AlarmState.ARMED, instance.getAlarmStatus().getState());
        // when
        instance.disarmAlarm();
        // then
        Assert.assertEquals(AlarmState.DISARMED, instance.getAlarmStatus().getState());
    }

    @Test
    public void trigger_alarm() throws InterruptedException {
        // given
        final PublishSubject<DeviceEvent> subject = PublishSubject.create();
        AlarmStatusService instance = new AlarmStatusService(0, subject);
        final DeviceEvent deviceReadEvent = new DeviceEvent(EventType.DEVICE_READ, new Date(), new Device("d1", DeviceType.SENSOR_MOTION), "true");
        Assert.assertEquals(AlarmState.DISARMED, instance.getAlarmStatus().getState());
        // when
        instance.armAlarm();
        subject.onNext(deviceReadEvent);
        // then
        Assert.assertEquals(AlarmState.COUNTDOWN, instance.getAlarmStatus().getState());
        Thread.sleep(5);
        Assert.assertEquals(AlarmState.TRIGGERED, instance.getAlarmStatus().getState());
    }

    @Test
    public void countdown_then_disarm() throws InterruptedException {
        // given
        final PublishSubject<DeviceEvent> subject = PublishSubject.create();
        AlarmStatusService instance = new AlarmStatusService(1, subject);
        final DeviceEvent deviceReadEvent = new DeviceEvent(EventType.DEVICE_READ, new Date(), new Device("d1", DeviceType.SENSOR_MOTION), "true");
        Assert.assertEquals(AlarmState.DISARMED, instance.getAlarmStatus().getState());
        // when
        instance.armAlarm();
        subject.onNext(deviceReadEvent);
        // then
        Assert.assertEquals(AlarmState.COUNTDOWN, instance.getAlarmStatus().getState());
        Thread.sleep(5);
        instance.disarmAlarm();
        Thread.sleep(5);
        Assert.assertEquals(AlarmState.DISARMED, instance.getAlarmStatus().getState());
    }

}

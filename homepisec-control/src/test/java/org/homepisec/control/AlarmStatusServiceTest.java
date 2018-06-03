package org.homepisec.control;

import io.reactivex.subjects.PublishSubject;
import org.awaitility.Awaitility;
import org.homepisec.control.core.AlarmStatusService;
import org.homepisec.control.core.alarm.AlarmState;
import org.homepisec.control.core.alarm.events.AlarmDisarmEvent;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceType;
import org.homepisec.control.rest.dto.EventType;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

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
    public void trigger_alarm() {
        // given
        final PublishSubject<DeviceEvent> subject = PublishSubject.create();
        AlarmStatusService instance = new AlarmStatusService(0, subject);
        final Device sensor = new Device("d1", DeviceType.SENSOR_MOTION);
        final DeviceEvent deviceReadEvent = new DeviceEvent(
                EventType.DEVICE_READ,
                System.currentTimeMillis(),
                sensor,
                "true"
        );
        Assert.assertEquals(AlarmState.DISARMED, instance.getAlarmStatus().getState());
        Assert.assertNull(instance.getAlarmStatus().getCountdown());
        Assert.assertNull(instance.getAlarmStatus().getTrigger());
        // when alarm armed and motion detected
        instance.armAlarm();
        subject.onNext(deviceReadEvent);
        // then trigger countdown
        Assert.assertEquals(AlarmState.COUNTDOWN, instance.getAlarmStatus().getState());
        Assert.assertEquals(sensor.getId(), instance.getAlarmStatus().getCountdown().getSource().getId());
        Assert.assertEquals(sensor.getType(), instance.getAlarmStatus().getCountdown().getSource().getType());
        // then trigger alarm
        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .until(() -> AlarmState.TRIGGERED.equals(instance.getAlarmStatus().getState()));
        Assert.assertEquals(sensor.getId(), instance.getAlarmStatus().getTrigger().getSource().getId());
        Assert.assertEquals(sensor.getType(), instance.getAlarmStatus().getTrigger().getSource().getType());
        // then disarm
        subject.onNext(new AlarmDisarmEvent(System.currentTimeMillis()));
        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .until(() -> AlarmState.DISARMED.equals(instance.getAlarmStatus().getState()));
        Assert.assertNull(instance.getAlarmStatus().getTrigger());
        Assert.assertNull(instance.getAlarmStatus().getCountdown());
    }

    @Test
    public void countdown_then_disarm() {
        // given
        final PublishSubject<DeviceEvent> subject = PublishSubject.create();
        AlarmStatusService instance = new AlarmStatusService(1, subject);
        final DeviceEvent deviceReadEvent = new DeviceEvent(
                EventType.DEVICE_READ,
                System.currentTimeMillis(),
                new Device("d1", DeviceType.SENSOR_MOTION),
                "true"
        );
        Assert.assertEquals(AlarmState.DISARMED, instance.getAlarmStatus().getState());
        // when alarm armed and motion detected
        instance.armAlarm();
        subject.onNext(deviceReadEvent);
        // then trigger countdown
        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .until(() -> AlarmState.COUNTDOWN.equals(instance.getAlarmStatus().getState()));
        // then disarm alarm
        instance.disarmAlarm();
        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .until(() -> AlarmState.DISARMED.equals(instance.getAlarmStatus().getState()));
    }

}

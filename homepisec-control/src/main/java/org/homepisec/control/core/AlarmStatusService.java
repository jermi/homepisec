package org.homepisec.control.core;

import org.homepisec.control.core.alarm.AlarmState;
import org.homepisec.control.core.alarm.AlarmStatus;
import org.homepisec.control.core.alarm.events.AlarmArmEvent;
import org.homepisec.control.core.alarm.events.AlarmCountdownEvent;
import org.homepisec.control.core.alarm.events.AlarmDisarmEvent;
import org.homepisec.control.core.alarm.events.AlarmTriggeredEvent;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

@Service
public class AlarmStatusService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AlarmStatus alarmStatus = new AlarmStatus();
    private final int alarmCountdownSeconds;
    private final PublishSubject<DeviceEvent> eventsSubject;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private final Disposable disposable;

    @Autowired
    public AlarmStatusService(
            @Value("${alarmCountdownSeconds}") int alarmCountdownSeconds,
            PublishSubject<DeviceEvent> eventsSubject
    ) {
        this.alarmCountdownSeconds = alarmCountdownSeconds;
        this.eventsSubject = eventsSubject;
        this.disposable = eventsSubject.subscribe(this::handleEvent);
    }

    @PreDestroy
    public void destroy() {
        disposable.dispose();
    }

    private void handleEvent(DeviceEvent event) {
        switch (event.getType()) {
            case DEVICE_READ:
                handleDeviceRead(event);
                break;
            case ALARM_DISARM:
                handleAlarmDisarm();
                break;
            case ALARM_ARM:
                handleAlarmArm();
                break;
            case ALARM_COUNTDOWN:
                handleAlarmCountdown(event.getPayload());
                break;
            case ALARM_TRIGGER:
                handleAlarmTrigger();
                break;
            default:
                break;
        }
    }

    private void handleAlarmArm() {
        if (AlarmState.DISARMED.equals(alarmStatus.getState())) {
            logger.info("arming alarm");
            alarmStatus.setState(AlarmState.ARMED);
        }
    }

    private void handleDeviceRead(DeviceEvent event) {
        final boolean isAlarmArmed = alarmStatus.getState().equals(AlarmState.ARMED);
        final boolean isDeviceMotionSensor = DeviceType.SENSOR_MOTION.equals(event.getDevice().getType());
        if (isAlarmArmed && isDeviceMotionSensor) {
            final Boolean isMotionDetected = Boolean.valueOf(event.getPayload());
            if (isMotionDetected) {
                eventsSubject.onNext(new AlarmCountdownEvent(
                        System.currentTimeMillis(),
                        event.getDevice().getId()
                ));
            }
        }
    }

    private void handleAlarmDisarm() {
        logger.info("disarming alarm");
        alarmStatus.setState(AlarmState.DISARMED);
        if (scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()) {
            logger.info("canceling alarm countdown");
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    private void handleAlarmCountdown(final String deviceTrigger) {
        if (alarmStatus.getState().equals(AlarmState.ARMED)) {
            logger.info("starting alarm countdown because of {}", deviceTrigger);
            alarmStatus.setState(AlarmState.COUNTDOWN);
            alarmStatus.setCountdownStart(new Date());
            final LocalDateTime countdownEndDateTime = LocalDateTime.now()
                    .plus(alarmCountdownSeconds, ChronoUnit.SECONDS);
            final Date countdownEnd = Date.from(countdownEndDateTime.atZone(ZoneId.systemDefault()).toInstant());
            alarmStatus.setCountdownEnd(countdownEnd);
            triggerAlarmAfterCountdown(deviceTrigger);
        }
    }

    private void triggerAlarmAfterCountdown(final String deviceTrigger) {
        final Runnable runnable = () -> {
            logger.info("triggering alarm because of {}", deviceTrigger);
            eventsSubject.onNext(new AlarmTriggeredEvent(
                    System.currentTimeMillis(),
                    deviceTrigger
            ));
        };
        scheduledFuture = scheduler.schedule(runnable, alarmCountdownSeconds, TimeUnit.SECONDS);
    }

    private void handleAlarmTrigger() {
        if (!AlarmState.TRIGGERED.equals(alarmStatus.getState())) {
            alarmStatus.setState(AlarmState.TRIGGERED);
            alarmStatus.setTriggerStart(new Date());
        }
    }

    public void disarmAlarm() {
        eventsSubject.onNext(new AlarmDisarmEvent(System.currentTimeMillis()));
    }

    public void armAlarm() {
        eventsSubject.onNext(new AlarmArmEvent(System.currentTimeMillis()));
    }

    public AlarmStatus getAlarmStatus() {
        return alarmStatus;
    }

}

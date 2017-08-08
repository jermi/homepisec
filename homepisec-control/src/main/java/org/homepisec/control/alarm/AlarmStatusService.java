package org.homepisec.control.alarm;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.alarm.events.AlarmArmEvent;
import org.homepisec.control.alarm.events.AlarmCountdownEvent;
import org.homepisec.control.alarm.events.AlarmDisarmEvent;
import org.homepisec.control.alarm.events.AlarmTriggeredEvent;
import org.homepisec.dto.DeviceType;
import org.homepisec.dto.EnrichedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class AlarmStatusService {

    private final AlarmStatus alarmStatus = new AlarmStatus();
    private final int alarmCountdownSeconds;
    private final PublishSubject<EnrichedEvent> eventsSubject;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private Disposable disposable;

    @Autowired
    public AlarmStatusService(@Value("${alarmCountdownSeconds}") int alarmCountdownSeconds, PublishSubject<EnrichedEvent> eventsSubject) {
        this.alarmCountdownSeconds = alarmCountdownSeconds;
        this.eventsSubject = eventsSubject;
    }

    @PostConstruct
    public void init() {
        disposable = eventsSubject.subscribe(this::handleEvent);
    }

    @PreDestroy
    public void destroy() {
        disposable.dispose();
    }

    private void handleEvent(EnrichedEvent event) {
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
                handleAlarmCountdown();
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
            alarmStatus.setState(AlarmState.ARMED);
        }
    }

    private void handleDeviceRead(EnrichedEvent event) {
        final boolean isAlarmArmed = alarmStatus.getState().equals(AlarmState.ARMED);
        final boolean isDeviceMotionSensor = DeviceType.SENSOR_MOTION.equals(event.getDevice().getType());
        if (isAlarmArmed && isDeviceMotionSensor) {
            final Boolean isMotionDetected = Boolean.valueOf(event.getPayload().toString());
            if (isMotionDetected) {
                eventsSubject.onNext(new AlarmCountdownEvent(new Date()));
            }
        }
    }

    private void handleAlarmDisarm() {
        alarmStatus.setState(AlarmState.DISARMED);
        if (scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    private void handleAlarmCountdown() {
        if (alarmStatus.getState().equals(AlarmState.ARMED)) {
            alarmStatus.setState(AlarmState.COUNTDOWN);
            alarmStatus.setCountdownStart(new Date());
            final LocalDateTime countdownEndDateTime = LocalDateTime.now()
                    .plus(alarmCountdownSeconds, ChronoUnit.SECONDS);
            final Date countdownEnd = Date.from(countdownEndDateTime.atZone(ZoneId.of("Europe/Warsaw")).toInstant());
            alarmStatus.setCountdownEnd(countdownEnd);
            delayAlarmTrigger();
        }
    }

    private void delayAlarmTrigger() {
        final Runnable runnable = () -> eventsSubject.onNext(new AlarmTriggeredEvent(new Date()));
        scheduledFuture = scheduler.schedule(runnable, alarmCountdownSeconds, TimeUnit.SECONDS);
    }

    private void handleAlarmTrigger() {
        if (!AlarmState.TRIGGERED.equals(alarmStatus.getState())) {
            alarmStatus.setState(AlarmState.TRIGGERED);
            alarmStatus.setTriggerStart(new Date());
        }
    }

    public void disarmAlarm() {
        eventsSubject.onNext(new AlarmDisarmEvent(new Date()));
    }

    public void armAlarm() {
        eventsSubject.onNext(new AlarmArmEvent(new Date()));
    }

    public AlarmStatus getAlarmStatus() {
        return alarmStatus;
    }

}

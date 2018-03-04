package org.homepisec.control.core;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.core.alarm.AlarmState;
import org.homepisec.control.core.alarm.AlarmStatus;
import org.homepisec.control.core.alarm.events.AlarmArmEvent;
import org.homepisec.control.core.alarm.events.AlarmCountdownEvent;
import org.homepisec.control.core.alarm.events.AlarmDisarmEvent;
import org.homepisec.control.core.alarm.events.AlarmTriggeredEvent;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AlarmStatus alarmStatus = new AlarmStatus();
    private final int alarmCountdownSeconds;
    private final PublishSubject<DeviceEvent> eventsSubject;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private volatile ScheduledFuture<?> scheduledFuture;
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
        try {
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
                    handleAlarmCountdown((AlarmCountdownEvent) event);
                    break;
                case ALARM_TRIGGER:
                    handleAlarmTrigger((AlarmTriggeredEvent) event);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("unexpected error for event " + event + ": " + e.getMessage(), e);
        }
    }

    private synchronized void handleAlarmArm() {
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
                        event.getDevice()
                ));
            }
        }
    }

    private synchronized void handleAlarmDisarm() {
        logger.info("disarming alarm");
        alarmStatus.setState(AlarmState.DISARMED);
        alarmStatus.setCountdownSource(null);
        alarmStatus.setTriggerStart(null);
        alarmStatus.setTriggerSource(null);
        alarmStatus.setCountdownStart(null);
        alarmStatus.setCountdownEnd(null);
        if (scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()) {
            logger.info("canceling alarm countdown");
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    private synchronized void handleAlarmCountdown(final AlarmCountdownEvent event) {
        if (alarmStatus.getState().equals(AlarmState.ARMED)) {
            logger.info("starting alarm countdown because of {}", event.getSource().getId());
            alarmStatus.setState(AlarmState.COUNTDOWN);
            alarmStatus.setCountdownStart(new Date());
            final LocalDateTime countdownEndDateTime = LocalDateTime.now()
                    .plus(alarmCountdownSeconds, ChronoUnit.SECONDS);
            final Date countdownEnd = Date.from(countdownEndDateTime.atZone(ZoneId.systemDefault()).toInstant());
            alarmStatus.setCountdownEnd(countdownEnd);
            alarmStatus.setCountdownSource(event.getSource());
            triggerAlarmAfterCountdown(event.getSource());
        }
    }

    private void triggerAlarmAfterCountdown(final Device source) {
        final Runnable runnable = () -> eventsSubject.onNext(new AlarmTriggeredEvent(
                System.currentTimeMillis(),
                source
        ));
        scheduledFuture = scheduler.schedule(runnable, alarmCountdownSeconds, TimeUnit.SECONDS);
    }

    private synchronized void handleAlarmTrigger(AlarmTriggeredEvent event) {
        if (!AlarmState.TRIGGERED.equals(alarmStatus.getState())) {
            final Device source = event.getSource();
            logger.info("triggering alarm because of {}", source.getId());
            alarmStatus.setState(AlarmState.TRIGGERED);
            alarmStatus.setTriggerStart(new Date());
            alarmStatus.setTriggerSource(source);
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

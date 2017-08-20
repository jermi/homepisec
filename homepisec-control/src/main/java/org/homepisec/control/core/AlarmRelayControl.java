package org.homepisec.control.core;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.rest.client.SensorApiControllerApi;
import org.homepisec.control.rest.client.SwitchRelayRequest;
import org.homepisec.dto.EnrichedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class AlarmRelayControl {

    private final SensorApiControllerApi apiClient;
    private final PublishSubject<EnrichedEvent> eventsSubject;
    private Disposable disposable;

    public AlarmRelayControl(SensorApiControllerApi apiClient, PublishSubject<EnrichedEvent> eventsSubject) {
        this.apiClient = apiClient;
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
            case ALARM_DISARM:
                handleAlarmDisarm();
                break;
            case ALARM_TRIGGER:
                handleAlarmTrigger();
                break;
            default:
                break;
        }
    }

    private void handleAlarmDisarm() {
        apiClient.switchRelayUsingPOST(new SwitchRelayRequest().id("relay-alarm").value(false));
    }

    private void handleAlarmTrigger() {
        apiClient.switchRelayUsingPOST(new SwitchRelayRequest().id("relay-alarm").value(true));
    }

}

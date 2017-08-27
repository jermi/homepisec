package org.homepisec.control.core;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.rest.client.api.SensorApiControllerApi;
import org.homepisec.control.rest.client.model.SwitchRelayRequest;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.stream.Stream;

@Service
public class AlarmRelayControl {

    private final SensorAppRegistry sensorAppRegistry;
    private final SensorApiControllerApi apiClient;
    private final Disposable disposable;

    public AlarmRelayControl(
            SensorAppRegistry sensorAppRegistry,
            SensorApiControllerApi apiClient,
            PublishSubject<DeviceEvent> eventsSubject
    ) {
        this.sensorAppRegistry = sensorAppRegistry;
        this.apiClient = apiClient;
        disposable = eventsSubject.subscribe(this::handleEvent);
    }

    @PreDestroy
    public void destroy() {
        disposable.dispose();
    }

    private void handleEvent(DeviceEvent event) {
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
        getAlarmRelaysIds().forEach(id -> invokeRelaySwitch(apiClient, id, false));
    }

    private void handleAlarmTrigger() {
        getAlarmRelaysIds().forEach(id -> invokeRelaySwitch(apiClient, id, true));
    }

    private Stream<String> getAlarmRelaysIds() {
        return sensorAppRegistry.getEndpoints()
                .stream()
                .flatMap(l -> l.getAlarmRelays().stream())
                .map(Device::getId);
    }

    private static void invokeRelaySwitch(SensorApiControllerApi apiClient, String id, boolean value) {
        apiClient.switchRelayUsingPOST(new SwitchRelayRequest().id(id).value(value));
    }

}

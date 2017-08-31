package org.homepisec.control.core;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.rest.client.ApiClient;
import org.homepisec.control.rest.client.api.SensorApiControllerApi;
import org.homepisec.control.rest.client.model.SwitchRelayRequest;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.SensorAppEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmRelayControl {

    private final RestTemplate restTemplate;
    private final EndpointRegistry endpointRegistry;
    private final Disposable disposable;

    public AlarmRelayControl(
            RestTemplate restTemplate, EndpointRegistry endpointRegistry,
            PublishSubject<DeviceEvent> eventsSubject
    ) {
        this.restTemplate = restTemplate;
        this.endpointRegistry = endpointRegistry;
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
        switchAlarmRelays(endpointRegistry.getEndpoints(), false);
    }

    private void handleAlarmTrigger() {
        switchAlarmRelays(endpointRegistry.getEndpoints(), true);
    }

    private void switchAlarmRelays(List<SensorAppEndpoint> endpoints, boolean value) {
        final Map<String, SensorApiControllerApi> clientCache = new HashMap<>();
        endpoints.forEach(e -> {
            final String url = e.getUrl();
            if (!clientCache.containsKey(url)) {
                clientCache.put(url, new SensorApiControllerApi(new ApiClient(restTemplate).setBasePath(url)));
            }
            final SensorApiControllerApi sensorApiClient = clientCache.get(url);
            e.getAlarmRelays().forEach(r -> {
                final SwitchRelayRequest request = new SwitchRelayRequest().id(r.getId()).value(value);
                sensorApiClient.switchRelayUsingPOST(request);
            });
        });
    }
}

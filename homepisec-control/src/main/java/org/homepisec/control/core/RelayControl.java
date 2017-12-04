package org.homepisec.control.core;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.rest.client.ApiClient;
import org.homepisec.control.rest.client.api.SensorApiControllerApi;
import org.homepisec.control.rest.client.model.SwitchRelayRequest;
import org.homepisec.control.rest.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.util.Optional;

@Service
public class RelayControl {

    private final RestTemplate restTemplate;
    private final EndpointRegistry endpointRegistry;
    private final Disposable disposable;
    private final PublishSubject<DeviceEvent> eventsSubject;

    public RelayControl(
            RestTemplate restTemplate,
            EndpointRegistry endpointRegistry,
            PublishSubject<DeviceEvent> eventsSubject
    ) {
        this.restTemplate = restTemplate;
        this.endpointRegistry = endpointRegistry;
        this.eventsSubject = eventsSubject;
        this.disposable = eventsSubject.subscribe(this::handleEvent);
    }

    @PreDestroy
    public void destroy() {
        disposable.dispose();
    }

    public void switchRelay(String relayId, boolean value) {
        final DeviceEvent event = new DeviceEvent(
                EventType.SWITCH_RELAY,
                System.currentTimeMillis(),
                new Device(relayId, DeviceType.RELAY),
                String.valueOf(value)
        );
        eventsSubject.onNext(event);
    }

    private void handleEvent(DeviceEvent event) {
        if (EventType.SWITCH_RELAY.equals(event.getType())) {
            handleSwitchRelayEvent(event);
        }
    }

    private void handleSwitchRelayEvent(DeviceEvent event) {
        final String deviceId = event.getDevice().getId();
        final boolean switchState = Boolean.parseBoolean(event.getPayload());
        final Optional<SensorAppEndpoint> endpoint = endpointRegistry.getEndpoints().stream()
                .filter(e -> e.getRelays().stream().anyMatch(r -> r.getId().equals(deviceId)))
                .findFirst();
        if (endpoint.isPresent()) {
            performPost(deviceId, switchState, endpoint.get());
        } else {
            throw new IllegalArgumentException("no endpoint with device id " + deviceId);
        }
    }

    private void performPost(String deviceId, boolean switchState, SensorAppEndpoint endpoint) {
        final SensorApiControllerApi api = new SensorApiControllerApi(new ApiClient(restTemplate).setBasePath(endpoint.getUrl()));
        final SwitchRelayRequest relayRequest = new SwitchRelayRequest();
        relayRequest.setId(deviceId);
        relayRequest.setValue(switchState);
        api.switchRelayUsingPOST(relayRequest);
    }

}

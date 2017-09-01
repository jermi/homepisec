package org.homepisec.sensor.core;

import org.homepisec.sensor.rest.client.api.EndpointControllerApi;
import org.homepisec.sensor.rest.client.model.Device;
import org.homepisec.sensor.rest.client.model.SensorAppEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SensorEndpointService {

    private final DeviceRegistryProvider deviceRegistryProvider;
    private final EndpointControllerApi endpointControllerApi;
    private final SensorAppUrlProvider sensorAppUrlProvider;

    public SensorEndpointService(
            DeviceRegistryProvider deviceRegistryProvider,
            EndpointControllerApi endpointControllerApi,
            SensorAppUrlProvider sensorAppUrlProvider) {
        this.deviceRegistryProvider = deviceRegistryProvider;
        this.endpointControllerApi = endpointControllerApi;
        this.sensorAppUrlProvider = sensorAppUrlProvider;
    }

    @Scheduled(fixedRateString = "${endpointUpdateFrequency}")
    public void updateEndpoint() {
        final DeviceRegistry deviceRegistry = deviceRegistryProvider.loadRegistry();
        final SensorAppEndpoint endpoint = new SensorAppEndpoint();
        endpoint.setUrl(sensorAppUrlProvider.getUrl());
        deviceRegistry.getRelays().forEach(r -> {
            final Device relayDevice = new Device().id(r.getId()).type(Device.TypeEnum.RELAY);
            endpoint.addRelaysItem(relayDevice);
        });
        deviceRegistry.getAlarmRelays().forEach(r -> {
            final Device alarmRelayDevice = new Device().id(r.getId()).type(Device.TypeEnum.RELAY);
            endpoint.addAlarmRelaysItem(alarmRelayDevice);
        });
        endpointControllerApi.addOrUpdateUsingPOST(endpoint);
    }

}

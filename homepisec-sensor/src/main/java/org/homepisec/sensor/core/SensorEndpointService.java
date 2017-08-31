package org.homepisec.sensor.core;

import org.homepisec.sensor.config.SensorApiEndpoints;
import org.homepisec.sensor.rest.client.api.EndpointControllerApi;
import org.homepisec.sensor.rest.client.model.Device;
import org.homepisec.sensor.rest.client.model.SensorAppEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class SensorEndpointService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DeviceRegistryProvider deviceRegistryProvider;
    private final EndpointControllerApi endpointControllerApi;
    private final int serverPort;

    public SensorEndpointService(
            DeviceRegistryProvider deviceRegistryProvider,
            EndpointControllerApi endpointControllerApi,
            @Value("${server.port}") int serverPort
    ) {
        this.deviceRegistryProvider = deviceRegistryProvider;
        this.endpointControllerApi = endpointControllerApi;
        this.serverPort = serverPort;
    }

    @Scheduled(fixedRateString = "${endpointUpdateFrequency}")
    public void updateEndpoint() {
        final DeviceRegistry deviceRegistry = deviceRegistryProvider.loadRegistry();
        final SensorAppEndpoint endpoint = new SensorAppEndpoint();
        endpoint.setUrl(determineUrl());
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

    private String determineUrl() {
        try {
            // TODO
            return "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + serverPort;
        } catch (UnknownHostException e) {
            final String msg = "unable to determine host address: " + e.getMessage();
            logger.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
    }

}

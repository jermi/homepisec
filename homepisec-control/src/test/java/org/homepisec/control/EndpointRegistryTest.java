package org.homepisec.control;

import org.homepisec.control.core.EndpointRegistry;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceType;
import org.homepisec.control.rest.dto.SensorAppEndpoint;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class EndpointRegistryTest {

    @Test
    public void add_endpoint() {
        // given
        final EndpointRegistry endpointRegistry = new EndpointRegistry(1);
        Assert.assertEquals(0, endpointRegistry.getEndpoints().size());
        final SensorAppEndpoint event = createEvent();
        // when
        endpointRegistry.addOrUpdate(event);
        // then
        Assert.assertEquals(1, endpointRegistry.getEndpoints().size());
        Assert.assertEquals("sensorUrl", endpointRegistry.getEndpoints().get(0).getUrl());
        Assert.assertEquals("r1", endpointRegistry.getEndpoints().get(0).getRelays().get(0).getId());
        Assert.assertEquals("r2", endpointRegistry.getEndpoints().get(0).getAlarmRelays().get(0).getId());
    }

    private static SensorAppEndpoint createEvent() {
        return new SensorAppEndpoint(
                "sensorUrl",
                Arrays.asList(new Device("r1", DeviceType.RELAY)),
                Arrays.asList(new Device("r2", DeviceType.RELAY))
        );
    }

    @Test
    public void remove_endpoint() throws InterruptedException {
        // given
        final EndpointRegistry endpointRegistry = new EndpointRegistry(-1);
        Assert.assertEquals(0, endpointRegistry.getEndpoints().size());
        final SensorAppEndpoint event = createEvent();
        // when
        endpointRegistry.addOrUpdate(event);
        Assert.assertEquals(1, endpointRegistry.getEndpoints().size());
        endpointRegistry.removeOldReadings();
        // then
        Assert.assertEquals(0, endpointRegistry.getEndpoints().size());
    }

}

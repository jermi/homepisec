package org.homepisec.control;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.core.SensorAppRegistry;
import org.homepisec.control.rest.dto.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

public class SensorAppRegistryTest {

    @Test
    public void add_endpoint() {
        // given
        final PublishSubject<DeviceEvent> eventsSubject = PublishSubject.create();
        final SensorAppRegistry sensorAppRegistry = new SensorAppRegistry(1, eventsSubject);
        Assert.assertEquals(0, sensorAppRegistry.getEndpoints().size());
        final DeviceEvent<SensorAppEndpoint> event = createEvent();
        // when
        eventsSubject.onNext(event);
        // then
        Assert.assertEquals(1, sensorAppRegistry.getEndpoints().size());
        Assert.assertEquals("sensorUrl", sensorAppRegistry.getEndpoints().get(0).getUrl());
        Assert.assertEquals("r1", sensorAppRegistry.getEndpoints().get(0).getRelays().get(0).getId());
        Assert.assertEquals("r2", sensorAppRegistry.getEndpoints().get(0).getAlarmRelays().get(0).getId());
    }

    private static DeviceEvent<SensorAppEndpoint> createEvent() {
        return new DeviceEvent<>(
                    EventType.DEVICE_READ,
                    new Date(),
                    new Device("sensorApp", DeviceType.SENSOR_APP),
                    new SensorAppEndpoint(
                            "sensorUrl",
                            Arrays.asList(new Device("r1", DeviceType.RELAY)),
                            Arrays.asList(new Device("r2", DeviceType.RELAY))
                    )
            );
    }

    @Test
    public void remove_endpoint() throws InterruptedException {
        // given
        final PublishSubject<DeviceEvent> eventsSubject = PublishSubject.create();
        final SensorAppRegistry sensorAppRegistry = new SensorAppRegistry(-1, eventsSubject);
        Assert.assertEquals(0, sensorAppRegistry.getEndpoints().size());
        final DeviceEvent<SensorAppEndpoint> event = createEvent();
        // when
        eventsSubject.onNext(event);
        Assert.assertEquals(1, sensorAppRegistry.getEndpoints().size());
        sensorAppRegistry.removeOldReadings();
        // then
        Assert.assertEquals(0, sensorAppRegistry.getEndpoints().size());
    }

}

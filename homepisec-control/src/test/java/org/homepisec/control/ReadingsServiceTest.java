package org.homepisec.control;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.dto.Device;
import org.homepisec.dto.DeviceReading;
import org.homepisec.dto.DeviceType;
import org.homepisec.dto.EnrichedEvent;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ReadingsServiceTest {

    private final PublishSubject<EnrichedEvent> eventsSubject = PublishSubject.create();

    @Test
    public void readEmptyEventsTest() {
        // given
        final ReadingsService instance = new ReadingsService(eventsSubject);
        // when
        final List<EnrichedEvent> events = instance.readEvents(0, 10);
        // then
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void readEventsOffsetCountTest() {
        // given
        final ReadingsService instance = new ReadingsService(eventsSubject);
        final Device device = new Device("d1", DeviceType.SENSOR_MOTION);
        // when
        instance.handleDeviceRead(Arrays.asList(
                new DeviceReading(device, "false"),
                new DeviceReading(device, "true"),
                new DeviceReading(device, "false")
        ));
        final List<EnrichedEvent> events = instance.readEvents(1, 1);
        // then
        Assert.assertEquals(1, events.size());
        Assert.assertEquals("true", events.get(0).getPayload());
    }

    @Test
    public void readEventsUnknownDeviceTest() {
        // given
        final ReadingsService instance = new ReadingsService(eventsSubject);
        final Device device = new Device("d1", DeviceType.SENSOR_MOTION);
        // when
        instance.handleDeviceRead(Arrays.asList(
                new DeviceReading(device, "false"),
                new DeviceReading(device, "true"),
                new DeviceReading(device, "false")
        ));
        final List<EnrichedEvent> events = instance.readEvents(0, 10);
        final Device device2 = instance.getDevice(device.getId());
        // then
        Assert.assertEquals(device.getId(), device2.getId());
        Assert.assertEquals(DeviceType.SENSOR_MOTION, device2.getType());
        Assert.assertEquals(3, events.size());
    }

}

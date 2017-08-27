package org.homepisec.control;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.core.ReadingsService;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceReading;
import org.homepisec.control.rest.dto.DeviceType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ReadingsServiceTest {

    private final PublishSubject<DeviceEvent> eventsSubject = PublishSubject.create();

    @Test
    public void readEmptyEventsTest() {
        // given
        final ReadingsService instance = new ReadingsService(1, eventsSubject);
        // when
        final List<DeviceEvent> events = instance.getReadings();
        // then
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void emitDeviceReadEvents() {
        // given
        final ReadingsService instance = new ReadingsService(1, eventsSubject);
        final Device device = createDevice();
        // when
        instance.emitDeviceReadEvent(createReadings(device));
        final List<DeviceEvent> events = instance.getReadings();
        // then
        Assert.assertEquals(1, events.size());
        Assert.assertEquals("false", events.get(0).getPayload());
    }

    @Test
    public void getDevices() {
        // given
        final ReadingsService instance = new ReadingsService(1, eventsSubject);
        final Device device = new Device("d1", DeviceType.SENSOR_MOTION);
        final Device device2 = new Device("d2", DeviceType.SENSOR_MOTION);
        // when
        instance.emitDeviceReadEvent(createReadings(device));
        instance.emitDeviceReadEvent(createReadings(device2));
        // then
        Assert.assertEquals(2, instance.getDevices().size());
    }

    private static Device createDevice() {
        return new Device("d1", DeviceType.SENSOR_MOTION);
    }

    private static List<DeviceReading> createReadings(Device device) {
        return Arrays.asList(
                new DeviceReading(device, "true"),
                new DeviceReading(device, "true"),
                new DeviceReading(device, "false")
        );
    }

    @Test
    public void removeOldReadings() {
        // given
        final ReadingsService instance = new ReadingsService(-1, eventsSubject);
        final Device device = createDevice();
        // when
        instance.emitDeviceReadEvent(createReadings(device));
        // then
        Assert.assertEquals(1, instance.getReadings().size());
        // when
        instance.removeOldReadings();
        // then
        Assert.assertEquals(0, instance.getReadings().size());
    }

}

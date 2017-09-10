package org.homepisec.control;

import org.homepisec.control.rest.api.DeviceEventsDeltaFilter;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceType;
import org.homepisec.control.rest.dto.EventType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DeviceEventsDeltaFilterTest {

    @Test
    public void does_not_pass_same_reading_value() {
        // given
        DeviceEventsDeltaFilter filter = new DeviceEventsDeltaFilter();
        Device d1 = new Device("d1", DeviceType.SENSOR_MOTION);
        DeviceEvent event1 = new DeviceEvent(EventType.DEVICE_READ, new Date(), d1, "false");
        // when
        boolean firstTest = filter.test(event1);
        boolean secondTest = filter.test(event1);
        // then
        Assert.assertTrue(firstTest);
        Assert.assertFalse(secondTest);
    }

    @Test
    public void passes_different_reading_value() {
        // given
        DeviceEventsDeltaFilter filter = new DeviceEventsDeltaFilter();
        Device d1 = new Device("d1", DeviceType.SENSOR_MOTION);
        DeviceEvent event1 = new DeviceEvent(EventType.DEVICE_READ, new Date(), d1, "false");
        DeviceEvent event2 = new DeviceEvent(EventType.DEVICE_READ, new Date(), d1, "true");
        // when
        boolean firstTest = filter.test(event1);
        boolean secondTest = filter.test(event2);
        // then
        Assert.assertTrue(firstTest);
        Assert.assertTrue(secondTest);
    }

}

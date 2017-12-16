package org.homepisec.sensor.test;

import org.homepisec.sensor.core.DeviceRegistry;
import org.homepisec.sensor.core.GpioDebounceService;
import org.homepisec.sensor.core.GpioProvider;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GpioDebounceServiceTest {

    private final Map<Integer, Boolean> pinValues = new ConcurrentHashMap<>();
    private final GpioProvider gpioProvider = new GpioProvider(null) {

        @Override
        public boolean readPin(int pin) {
            return pinValues.putIfAbsent(pin, false);
        }

        @Override
        public void writePin(int pin, boolean value) {
            pinValues.put(pin, value);
        }

        @Override
        public void setPinDirection(int pin, Direction direction) {
            // noop
        }
    };
    private final GpioDebounceService instance = new GpioDebounceService(gpioProvider);

    @Test
    public void debounce_to_false() throws InterruptedException {
        final int pin = 1;
        final DeviceRegistry.DeviceGpio deviceGpio = new DeviceRegistry.DeviceGpio(
                "test-device",
                pin,
                25,
                false
        );
        // when
        gpioProvider.writePin(pin, true);
        instance.registerDebounce(deviceGpio);
        // then
        Thread.sleep(5);
        Assert.assertEquals(true, pinValues.get(pin));
        Thread.sleep(21);
        Assert.assertEquals(false, pinValues.get(pin));
    }

    @Test
    public void debounce_to_true() throws InterruptedException {
        final int pin = 3;
        final DeviceRegistry.DeviceGpio deviceGpio = new DeviceRegistry.DeviceGpio(
                "test-device",
                pin,
                25,
                true
        );
        // when
        gpioProvider.writePin(pin, false);
        instance.registerDebounce(deviceGpio);
        // then
        Thread.sleep(5);
        Assert.assertEquals(false, pinValues.get(pin));
        Thread.sleep(21);
        Assert.assertEquals(true, pinValues.get(pin));
    }

    @Test
    public void no_debounce() throws InterruptedException {
        final int pin = 2;
        final DeviceRegistry.DeviceGpio deviceGpio = new DeviceRegistry.DeviceGpio(
                "test-device",
                pin,
                null,
                false
        );
        // when
        gpioProvider.writePin(pin, true);
        instance.registerDebounce(deviceGpio);
        Thread.sleep(5);
        // then
        Assert.assertEquals(true, pinValues.get(pin));
    }

}

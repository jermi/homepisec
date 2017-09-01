package org.homepisec.sensor.test;

import org.homepisec.sensor.core.SensorAppUrlProvider;
import org.junit.Assert;
import org.junit.Test;

import java.net.SocketException;

public class SensorAppUrlProviderTest {

    @Test
    public void getHttpUrlNoIpPreferred() throws SocketException {
        final SensorAppUrlProvider instance = new SensorAppUrlProvider(false, null, 1234);
        final String url = instance.getUrl();
        Assert.assertTrue(
                url.startsWith("http://192.168.")
                || url.startsWith("http://10.")
                || url.startsWith("http://172.16.")
        );
    }

    @Test
    public void getHttpsUrlIpPreferred() throws SocketException {
        final SensorAppUrlProvider instance = new SensorAppUrlProvider(true, "1.1.1.1", 1234);
        final String url = instance.getUrl();
        Assert.assertEquals("https://1.1.1.1:1234", url);
    }

}

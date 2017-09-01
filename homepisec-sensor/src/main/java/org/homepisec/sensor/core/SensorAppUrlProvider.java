package org.homepisec.sensor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SensorAppUrlProvider {

    private static final IpComparator IP_COMPARATOR = new IpComparator();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final boolean preferHttps;
    private final String preferredIp;
    private final int serverPort;

    public SensorAppUrlProvider(
            @Value("${preferHttps:#{false}") Boolean preferHttps,
            @Value("${preferredIp:#{null}") String preferredIp,
            @Value("${server.port}") int serverPort
    ) {
        this.preferHttps = preferHttps;
        this.preferredIp = preferredIp;
        this.serverPort = serverPort;
    }

    public String getUrl() {
        final String proto = preferHttps ? "https://" : "http://";
        final String ip = preferredIp != null ? preferredIp : getHostIps().get(0);
        return proto + ip + ":" + serverPort;
    }

    protected List<String> getHostIps() {
        try {
            final List<InetAddress> ips = new ArrayList<>();
            final Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i instanceof Inet4Address) {
                        ips.add(i);
                    }
                }
            }
            ips.sort(IP_COMPARATOR);
            return ips.stream().map(InetAddress::getHostAddress).collect(Collectors.toList());
        } catch (Exception e) {
            final String msg = "unable to get host ips: " + e.getMessage();
            logger.error(msg, e);
            throw new IllegalStateException(msg);
        }
    }

    public static class IpComparator implements Comparator<InetAddress> {

        @Override
        public int compare(InetAddress o1, InetAddress o2) {
            final boolean o1Local = o1.isSiteLocalAddress();
            final boolean o2Local = o2.isSiteLocalAddress();
            if (o1Local && !o2Local) {
                return -1;
            } else if (!o1Local && o2Local) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}

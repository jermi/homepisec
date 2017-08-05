package org.homepisec.sensor;

import org.homepisec.dto.Device;
import org.homepisec.dto.DeviceReading;
import org.homepisec.dto.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
public class SensorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ControlApiRestClient controlApiRestClient;

    @Autowired
    public SensorService(ControlApiRestClient controlApiRestClient) {
        this.controlApiRestClient = controlApiRestClient;
    }

    @Scheduled(fixedRate = 5000)
    public void sendDataToControl() {
        logger.info("sending data to control");
        int i = (int) (Math.random() * 10);
        List<DeviceReading> readings = new ArrayList<>();
        while (i-- > 0) {
            final DeviceReading reading = new DeviceReading(new Device("id1", DeviceType.SENSOR_MOTION), (Math.random() > 0.5) + "");
            readings.add(reading);
        }
        controlApiRestClient.postReadings(readings);
    }

}

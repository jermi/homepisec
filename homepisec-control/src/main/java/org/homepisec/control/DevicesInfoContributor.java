package org.homepisec.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class DevicesInfoContributor implements InfoContributor{

    private final ReadingsService readingsService;

    @Autowired
    public DevicesInfoContributor(ReadingsService readingsService) {
        this.readingsService = readingsService;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("devices", readingsService.getDevices());
    }
}

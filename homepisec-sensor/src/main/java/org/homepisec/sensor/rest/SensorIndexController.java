package org.homepisec.sensor.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorIndexController {

    @RequestMapping("/")
    public String index() {
        return "homepisec-sensor";
    }

}

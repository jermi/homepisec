package org.homepisec.control.rest.api;

import org.homepisec.control.config.ControlApiEndpoints;
import org.homepisec.control.core.ReadingsService;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.EventDeviceReading;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ControlApiEndpoints.API)
@Validated
public class ReadingsController {

    private final ReadingsService readingsService;
    private final ReadingsSseEmiterBuilder readingsSseEmiterBuilder;

    public ReadingsController(
            ReadingsService readingsService,
            ReadingsSseEmiterBuilder readingsSseEmiterBuilder
    ) {
        this.readingsService = readingsService;
        this.readingsSseEmiterBuilder = readingsSseEmiterBuilder;
    }

    @RequestMapping(
            value = ControlApiEndpoints.READINGS,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public boolean postReadings(@RequestBody @Valid EventDeviceReading event) {
        readingsService.emitDeviceReadEvent(event.getPayload());
        return true;
    }

    @RequestMapping(
            value = ControlApiEndpoints.READINGS,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public List<DeviceEvent> getReadings() {
        return readingsService.getReadings();
    }

    @RequestMapping(
            value = ControlApiEndpoints.READINGS + "/events",
            method = RequestMethod.GET
    )
    public SseEmitter subscribeUpdates() {
        return readingsSseEmiterBuilder.create();
    }

}

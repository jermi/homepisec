package org.homepisec.control.rest.api;

import org.homepisec.control.core.ReadingsService;
import org.homepisec.dto.ApiEndpoints;
import org.homepisec.dto.EnrichedEvent;
import org.homepisec.dto.EventDeviceReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.API)
@Validated
public class ReadingsController {

    private final ReadingsService readingsService;

    @Autowired
    public ReadingsController(ReadingsService readingsService) {
        this.readingsService = readingsService;
    }

    @RequestMapping(
            value = ApiEndpoints.READINGS,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public boolean postReadings(@RequestBody @Valid EventDeviceReading event) {
        readingsService.handleDeviceRead(event.getPayload());
        return true;
    }

    @RequestMapping(
            value = ApiEndpoints.READINGS,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public List<EnrichedEvent> getReadings() {
        return readingsService.getReadings();
    }

}
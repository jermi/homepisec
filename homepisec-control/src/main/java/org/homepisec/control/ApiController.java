package org.homepisec.control;

import org.homepisec.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.API)
@Validated
public class ApiController {

    private final ReadingsService readingsService;

    @Autowired
    public ApiController(ReadingsService readingsService) {
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
    public List<EnrichedEvent> getReadings(
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "count", required = false) Integer count
    ) {
        if (offset == null) {
            offset = 0;
        }
        if (count == null) {
            count = 10;
        }
        return readingsService.readEvents(offset, count);
    }

}

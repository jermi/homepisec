package org.homepisec.control.rest;

import org.homepisec.control.core.ReadingsService;
import org.homepisec.control.core.alarm.AlarmStatus;
import org.homepisec.control.core.AlarmStatusService;
import org.homepisec.dto.ApiEndpoints;
import org.homepisec.dto.EnrichedEvent;
import org.homepisec.dto.EventDeviceReading;
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
    private final AlarmStatusService alarmStatusService;

    @Autowired
    public ApiController(ReadingsService readingsService, AlarmStatusService alarmStatusService) {
        this.readingsService = readingsService;
        this.alarmStatusService = alarmStatusService;
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

    @RequestMapping(
            value = ApiEndpoints.ALARM,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public AlarmStatus getAlarmStatus() {
        return alarmStatusService.getAlarmStatus();
    }

    @RequestMapping(
            value = ApiEndpoints.ALARM_ARM,
            method = RequestMethod.POST,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public AlarmStatus postAlarmArm() {
        alarmStatusService.armAlarm();
        return alarmStatusService.getAlarmStatus();
    }

    @RequestMapping(
            value = ApiEndpoints.ALARM_DISARM,
            method = RequestMethod.POST,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public AlarmStatus postAlarmDisarm() {
        alarmStatusService.disarmAlarm();
        return alarmStatusService.getAlarmStatus();
    }

}

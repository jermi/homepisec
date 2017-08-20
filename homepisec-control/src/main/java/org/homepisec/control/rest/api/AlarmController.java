package org.homepisec.control.rest.api;

import org.homepisec.control.core.AlarmStatusService;
import org.homepisec.control.core.alarm.AlarmStatus;
import org.homepisec.dto.ApiEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoints.API)
@Validated
public class AlarmController {

    private final AlarmStatusService alarmStatusService;

    @Autowired
    public AlarmController(AlarmStatusService alarmStatusService) {
        this.alarmStatusService = alarmStatusService;
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
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public AlarmStatus postAlarmArm() {
        alarmStatusService.armAlarm();
        return alarmStatusService.getAlarmStatus();
    }

    @RequestMapping(
            value = ApiEndpoints.ALARM_DISARM,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public AlarmStatus postAlarmDisarm() {
        alarmStatusService.disarmAlarm();
        return alarmStatusService.getAlarmStatus();
    }

}

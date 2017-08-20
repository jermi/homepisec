package org.homepisec.control.rest.api;

import org.homepisec.control.config.ControlApiEndpoints;
import org.homepisec.control.core.AlarmStatusService;
import org.homepisec.control.core.alarm.AlarmStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControlApiEndpoints.API)
@Validated
public class AlarmController {

    private final AlarmStatusService alarmStatusService;

    @Autowired
    public AlarmController(AlarmStatusService alarmStatusService) {
        this.alarmStatusService = alarmStatusService;
    }

    @RequestMapping(
            value = ControlApiEndpoints.ALARM,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public AlarmStatus getAlarmStatus() {
        return alarmStatusService.getAlarmStatus();
    }

    @RequestMapping(
            value = ControlApiEndpoints.ALARM_ARM,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public AlarmStatus postAlarmArm() {
        alarmStatusService.armAlarm();
        return alarmStatusService.getAlarmStatus();
    }

    @RequestMapping(
            value = ControlApiEndpoints.ALARM_DISARM,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public AlarmStatus postAlarmDisarm() {
        alarmStatusService.disarmAlarm();
        return alarmStatusService.getAlarmStatus();
    }

}

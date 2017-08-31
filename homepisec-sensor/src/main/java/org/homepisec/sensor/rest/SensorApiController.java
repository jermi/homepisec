package org.homepisec.sensor.rest;

import org.homepisec.sensor.config.SensorApiEndpoints;
import org.homepisec.sensor.core.DeviceRegistry;
import org.homepisec.sensor.core.RelayService;
import org.homepisec.sensor.rest.dto.SwitchRelayRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(SensorApiEndpoints.API)
@Validated
public class SensorApiController {

    private final RelayService relayService;

    public SensorApiController(RelayService relayService) {
        this.relayService = relayService;
    }

    @RequestMapping(
            value = SensorApiEndpoints.RELAYS,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public List<DeviceRegistry.DeviceGpio> getRelays() {
        return relayService.getAllRelays();
    }

    @RequestMapping(
            value = SensorApiEndpoints.RELAYS,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public boolean switchRelay(@RequestBody @Valid @NotNull SwitchRelayRequest relayRequest) {
        return relayService.getAllRelays()
                .stream()
                .filter(r -> r.getId().equals(relayRequest.getId()))
                .findFirst()
                .map(r -> relayService.switchRelay(r, relayRequest.getValue()))
                .orElse(false);
    }

}

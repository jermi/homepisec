package org.homepisec.sensor.rest;

import org.hibernate.validator.constraints.NotEmpty;
import org.homepisec.dto.ApiEndpoints;
import org.homepisec.sensor.core.DeviceRegistry;
import org.homepisec.sensor.core.RelayService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiEndpoints.API)
@Validated
public class SensorApiController {

    private final RelayService relayService;

    public SensorApiController(RelayService relayService) {
        this.relayService = relayService;
    }

    @RequestMapping(
            value = ApiEndpoints.RELAYS,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public List<DeviceRegistry.DeviceGpio> getRelays() {
        return relayService.getAllRelays();
    }

    @RequestMapping(
            value = ApiEndpoints.RELAYS,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public boolean switchRelay(@RequestParam("id") @NotEmpty String relayId, @RequestParam("value") Boolean value) {
        final Optional<DeviceRegistry.DeviceGpio> relay = relayService.getAllRelays().stream().filter(r -> r.getId().equals(relayId)).findFirst();
        return relay
                .map(r -> relayService.switchRelay(r, value))
                .orElse(false);
    }


}

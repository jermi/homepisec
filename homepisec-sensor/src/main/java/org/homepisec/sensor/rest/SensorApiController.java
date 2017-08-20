package org.homepisec.sensor.rest;

import org.homepisec.dto.ApiEndpoints;
import org.homepisec.sensor.core.DeviceRegistry;
import org.homepisec.sensor.core.RelayService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    public static class SwitchRelayRequest {
        @NotNull
        public String id;
        @NotNull
        public Boolean value;
    }

    @RequestMapping(
            value = ApiEndpoints.RELAYS,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public boolean switchRelay(@RequestBody @Valid @NotNull SwitchRelayRequest relayRequest) {
        final Optional<DeviceRegistry.DeviceGpio> relay = relayService.getAllRelays().stream().filter(r -> r.getId().equals(relayRequest.id)).findFirst();
        return relay
                .map(r -> relayService.switchRelay(r, relayRequest.value))
                .orElse(false);
    }


}

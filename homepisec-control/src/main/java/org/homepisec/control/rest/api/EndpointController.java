package org.homepisec.control.rest.api;

import org.homepisec.control.config.ControlApiEndpoints;
import org.homepisec.control.core.EndpointRegistry;
import org.homepisec.control.rest.dto.SensorAppEndpoint;
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
@Validated
@RequestMapping(ControlApiEndpoints.API)
public class EndpointController {

    private final EndpointRegistry endpointRegistry;

    public EndpointController(EndpointRegistry endpointRegistry) {
        this.endpointRegistry = endpointRegistry;
    }

    @RequestMapping(
            value = ControlApiEndpoints.ENDPOINTS,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public List<SensorAppEndpoint> getEndpoints() {
        return endpointRegistry.getEndpoints();
    }

    @RequestMapping(
            value = ControlApiEndpoints.ENDPOINTS,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public boolean addOrUpdate(@RequestBody @Valid @NotNull SensorAppEndpoint endpoint) {
        endpointRegistry.addOrUpdate(endpoint);
        return true;
    }

}

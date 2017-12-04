package org.homepisec.control.rest.api;

import org.homepisec.control.config.ControlApiEndpoints;
import org.homepisec.control.core.RelayControl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControlApiEndpoints.API + ControlApiEndpoints.RELAYS)
public class RelayController {

    private final RelayControl relayControl;

    public RelayController(RelayControl relayControl) {
        this.relayControl = relayControl;
    }

    @RequestMapping(
            value = "/switch",
            method = RequestMethod.POST
    )
    public void switchRelay(String relayId, Boolean value) {
        relayControl.switchRelay(relayId, value);
    }

}

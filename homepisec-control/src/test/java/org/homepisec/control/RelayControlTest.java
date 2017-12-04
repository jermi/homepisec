package org.homepisec.control;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.core.AlarmRelayControl;
import org.homepisec.control.core.EndpointRegistry;
import org.homepisec.control.core.RelayControl;
import org.homepisec.control.rest.dto.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class RelayControlTest {

    @Test
    public void switch_relay() {
        // given
        final PublishSubject<DeviceEvent> subject = PublishSubject.create();
        final String relay1Id = "relay1";
        final String relay2Id = "relay2";
        final String endpointUrl = "http://nowhere.org:8080";
        final SensorAppEndpoint sensorAppEndpoint = new SensorAppEndpoint(
                endpointUrl,
                Arrays.asList(
                        new Device(relay1Id, DeviceType.RELAY),
                        new Device(relay2Id, DeviceType.RELAY)
                ),
                Collections.emptyList()
        );
        final EndpointRegistry endpointRegistry = new EndpointRegistry(1) {
            @Override
            public List<SensorAppEndpoint> getEndpoints() {
                return Collections.singletonList(sensorAppEndpoint);
            }
        };
        final RestTemplate restTemplate = new RestTemplate();
        final MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        new RelayControl(
                restTemplate,
                endpointRegistry,
                subject
        );
        // expect
        server
                .expect(once(), requestTo(endpointUrl + "/api/relays"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("{\"id\":\"" + relay1Id + "\",\"value\":true}"))
                .andRespond(withSuccess("true", MediaType.APPLICATION_JSON_UTF8));
        server
                .expect(once(), requestTo(endpointUrl + "/api/relays"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("{\"id\":\"" + relay1Id + "\",\"value\":false}"))
                .andRespond(withSuccess("false", MediaType.APPLICATION_JSON_UTF8));
        // when
        subject.onNext(new DeviceEvent(
                EventType.SWITCH_RELAY,
                System.currentTimeMillis(),
                new Device(relay1Id, DeviceType.RELAY),
                "true"
        ));
        subject.onNext(new DeviceEvent(
                EventType.SWITCH_RELAY,
                System.currentTimeMillis(),
                new Device(relay1Id, DeviceType.RELAY),
                "false"
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fail_with_invalid_relay_id() {
        // given
        final PublishSubject<DeviceEvent> subject = PublishSubject.create();
        final SensorAppEndpoint sensorAppEndpoint = new SensorAppEndpoint(
                "http://nowhere.org:8080",
                Collections.emptyList(),
                Collections.emptyList()
        );
        final EndpointRegistry endpointRegistry = new EndpointRegistry(1) {
            @Override
            public List<SensorAppEndpoint> getEndpoints() {
                return Collections.singletonList(sensorAppEndpoint);
            }
        };
        final RestTemplate restTemplate = new RestTemplate();
        new RelayControl(
                restTemplate,
                endpointRegistry,
                subject
        );
        // when
        subject.onNext(new DeviceEvent(
                EventType.SWITCH_RELAY,
                System.currentTimeMillis(),
                new Device("invalid-relay-id", DeviceType.RELAY),
                "true"
        ));
        // then fail
    }

}

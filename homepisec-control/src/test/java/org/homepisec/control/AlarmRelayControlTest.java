package org.homepisec.control;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.core.AlarmRelayControl;
import org.homepisec.control.core.EndpointRegistry;
import org.homepisec.control.core.alarm.events.AlarmDisarmEvent;
import org.homepisec.control.core.alarm.events.AlarmTriggeredEvent;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceType;
import org.homepisec.control.rest.dto.SensorAppEndpoint;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class AlarmRelayControlTest {

    @Test
    public void switch_relay_alarm_trigger_and_disarm() throws InterruptedException {
        // given
        final PublishSubject<DeviceEvent> subject = PublishSubject.create();
        final String alarmRelay1Id = "alarm-relay";
        final String alarmRelay2Id = "alarm-relay2";
        final String endpointUrl = "http://nowhere.org:8080";
        final SensorAppEndpoint sensorAppEndpoint = new SensorAppEndpoint(
                endpointUrl,
                Collections.emptyList(),
                Arrays.asList(
                        new Device(alarmRelay1Id, DeviceType.RELAY),
                        new Device(alarmRelay2Id, DeviceType.RELAY)
                )
        );
        final EndpointRegistry endpointRegistry = new EndpointRegistry(1) {
            @Override
            public List<SensorAppEndpoint> getEndpoints() {
                return Collections.singletonList(sensorAppEndpoint);
            }
        };
        final RestTemplate restTemplate = new RestTemplate();
        final MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        new AlarmRelayControl(
                restTemplate,
                endpointRegistry,
                subject
        );
        // expect
        server
                .expect(once(), requestTo(endpointUrl + "/api/relays"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("{\"id\":\"" + alarmRelay1Id + "\",\"value\":true}"))
                .andRespond(withSuccess("true", MediaType.APPLICATION_JSON_UTF8));
        server
                .expect(once(), requestTo(endpointUrl + "/api/relays"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("{\"id\":\"" + alarmRelay2Id + "\",\"value\":true}"))
                .andRespond(withSuccess("true", MediaType.APPLICATION_JSON_UTF8));
        server
                .expect(once(), requestTo(endpointUrl + "/api/relays"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("{\"id\":\"" + alarmRelay1Id + "\",\"value\":false}"))
                .andRespond(withSuccess("true", MediaType.APPLICATION_JSON_UTF8));
        server
                .expect(once(), requestTo(endpointUrl + "/api/relays"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("{\"id\":\"" + alarmRelay2Id + "\",\"value\":false}"))
                .andRespond(withSuccess("true", MediaType.APPLICATION_JSON_UTF8));
        // when
        subject.onNext(new AlarmTriggeredEvent(System.currentTimeMillis(), "motion-sensor-1"));
        subject.onNext(new AlarmDisarmEvent(System.currentTimeMillis()));
    }

}

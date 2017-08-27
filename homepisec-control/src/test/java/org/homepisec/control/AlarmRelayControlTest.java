package org.homepisec.control;

import io.reactivex.subjects.PublishSubject;
import org.homepisec.control.core.AlarmRelayControl;
import org.homepisec.control.core.SensorAppRegistry;
import org.homepisec.control.core.alarm.events.AlarmDisarmEvent;
import org.homepisec.control.core.alarm.events.AlarmTriggeredEvent;
import org.homepisec.control.rest.client.api.SensorApiControllerApi;
import org.homepisec.control.rest.client.model.SwitchRelayRequest;
import org.homepisec.control.rest.dto.Device;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.dto.DeviceType;
import org.homepisec.control.rest.dto.SensorAppEndpoint;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import java.util.*;

public class AlarmRelayControlTest {

    @Test
    public void switch_relay_alarm_trigger_and_disarm() throws InterruptedException {
        // given
        final PublishSubject<DeviceEvent> subject = PublishSubject.create();
        final String alarmRelay1Id = "alarm-relay";
        final String alarmRelay2Id = "alarm-relay2";
        final Map<String, Boolean> relayStatus = new HashMap<>();
        relayStatus.put(alarmRelay1Id, false);
        relayStatus.put(alarmRelay2Id, false);
        final SensorAppEndpoint sensorAppEndpoint = new SensorAppEndpoint(
                "url",
                Collections.emptyList(),
                Arrays.asList(
                        new Device("motion-sensor", DeviceType.SENSOR_MOTION),
                        new Device(alarmRelay1Id, DeviceType.RELAY),
                        new Device(alarmRelay2Id, DeviceType.RELAY)
                )
        );
        final SensorAppRegistry sensorAppRegistry = new SensorAppRegistry(1, subject) {
            @Override
            public List<SensorAppEndpoint> getEndpoints() {
                return Collections.singletonList(sensorAppEndpoint);
            }
        };
        new AlarmRelayControl(
                sensorAppRegistry,
                new SensorApiControllerApi() {
                    @Override
                    public Boolean switchRelayUsingPOST(SwitchRelayRequest relayRequest) throws RestClientException {
                        return relayStatus.put(relayRequest.getId(), relayRequest.getValue());
                    }
                },
                subject
        );
        // when
        subject.onNext(new AlarmTriggeredEvent(new Date()));
        // then
        Assert.assertEquals(true, relayStatus.get(alarmRelay1Id));
        Assert.assertEquals(true, relayStatus.get(alarmRelay2Id));
        // when
        subject.onNext(new AlarmDisarmEvent(new Date()));
        // then
        Assert.assertEquals(false, relayStatus.get(alarmRelay1Id));
        Assert.assertEquals(false, relayStatus.get(alarmRelay2Id));
    }

}

//package org.homepisec.sensor.rest;
//
//import org.homepisec.dto.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//@Component
//public class ControlApiRestClient {
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//    private final String controlApiEndpoint;
//    private final RestTemplate restTemplate;
//
//    @Autowired
//    public ControlApiRestClient(
//            @Value("${controlApiEndpoint}") String controlApiEndpoint,
//            RestTemplate restTemplate
//    ) {
//        this.controlApiEndpoint = controlApiEndpoint;
//        this.restTemplate = restTemplate;
//    }
//
//    public boolean postReadings(List<DeviceReading> readings) {
//        final HttpEntity<EventDeviceReading> requestEntity = new HttpEntity<>(new EventDeviceReading(readings), getJsonRequestHeaders());
//        final ResponseEntity<Boolean> response = restTemplate.exchange(controlApiEndpoint + ApiEndpoints.API + ApiEndpoints.READINGS, HttpMethod.POST, requestEntity, Boolean.class);
//        if (HttpStatus.OK.equals(response.getStatusCode())) {
//            return response.getBody();
//        }
//        logger.info("received invalid http response {}", response.getStatusCode());
//        return false;
//    }
//
//    private static MultiValueMap<String, String> getJsonRequestHeaders() {
//        final MultiValueMap<String, String> headers = new HttpHeaders();
//        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
//        return headers;
//    }
//
//}

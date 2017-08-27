package org.homepisec.control.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.reactivex.subjects.PublishSubject;
import org.apache.http.impl.client.HttpClientBuilder;
import org.homepisec.control.rest.dto.DeviceEvent;
import org.homepisec.control.rest.client.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ControlAppBeans {

    @Bean
    RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(10000);
        factory.setHttpClient(HttpClientBuilder.create().disableCookieManagement().build());
        return new RestTemplate(factory);
    }

    @Primary
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                ;
    }

    @Bean
    PublishSubject<DeviceEvent> eventsSubject() {
        return PublishSubject.create();
    }

    @Bean
    @Autowired
    ApiClient apiClient(RestTemplate restTemplate) {
        return new ApiClient(restTemplate).setBasePath("http://rpi:7000/");
    }

}

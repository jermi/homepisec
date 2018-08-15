package org.homepisec.sensor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.impl.client.HttpClientBuilder;
import org.homepisec.sensor.rest.client.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SensorAppBeans {

    @Value("${basePath:http://rpi:8080/}")
    String basePath;

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
                .enable(SerializationFeature.INDENT_OUTPUT)
                ;
    }

    @Primary
    @Bean
    @Autowired
    ApiClient apiClient(RestTemplate restTemplate) {
        return new ApiClient(restTemplate).setBasePath(basePath);
    }

}

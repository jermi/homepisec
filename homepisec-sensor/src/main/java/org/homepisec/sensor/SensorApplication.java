package org.homepisec.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@SpringBootApplication
@EnableScheduling
@ComponentScan("org.homepisec.sensor")
public class SensorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SensorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

	@Bean
	RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(10000);
		return new RestTemplate(factory);
	}

	@Bean
	ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

}

package com.siav_pisip.siav_web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	WebClient webClient(WebClient.Builder builder) {
		return builder.baseUrl("http://localhost:8080/api/siav").build();
	}
}
package com.implintrakt.Impl.interakt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class Configs {
    @Bean
    public RestClient.Builder restClient() {
        return RestClient.builder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Simple object creation
    }
}

package com.apigatewayplatform.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced // tells Spring to resolve user-service via Eureka instead of DNS.
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}

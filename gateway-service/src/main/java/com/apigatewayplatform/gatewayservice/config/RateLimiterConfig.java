package com.apigatewayplatform.gatewayservice.config;

import com.apigatewayplatform.gatewayservice.rateLimiter.FixedWindowRateLimiter;
import com.apigatewayplatform.gatewayservice.rateLimiter.RateLimiter;
import com.apigatewayplatform.gatewayservice.rateLimiter.SlidingWindowRateLimiter;
import com.apigatewayplatform.gatewayservice.rateLimiter.TokenBucketRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RateLimiterConfig {

    @Value("${rate-limit.algorithm}")
    private String algorithm;

    @Bean
    @Primary
    public RateLimiter rateLimiter(
            FixedWindowRateLimiter fixed,
            SlidingWindowRateLimiter sliding,
            TokenBucketRateLimiter token) {

        return switch (algorithm.toLowerCase()) {
            case "fixed" -> fixed;
            case "sliding" -> sliding;
            case "token" -> token;
            default -> throw new IllegalArgumentException("Unknown algorithm");
        };
    }
}

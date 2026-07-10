package com.apigatewayplatform.gatewayservice.rateLimiter;

import reactor.core.publisher.Mono;

public interface RateLimiter {

    Mono<Boolean> isAllowed(String username);
}

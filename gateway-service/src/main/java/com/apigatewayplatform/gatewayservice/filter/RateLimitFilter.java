package com.apigatewayplatform.gatewayservice.filter;

import com.apigatewayplatform.gatewayservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RateLimitFilter implements WebFilter {

    private final ReactiveStringRedisTemplate redisTemplate;

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        String token = authHeader.substring(7);

        String username = jwtUtil.extractUsername(token);

        String redisKey = "rate:" + username;

        return redisTemplate.opsForValue().increment(redisKey)
                .flatMap(count -> {

                    Mono<Long> countMono = Mono.just(count);

                    if (count == 1) {
                        // Set expiration time for the key if it's the first request
                        countMono = redisTemplate.expire(redisKey, Duration.ofSeconds(10))
                                .thenReturn(count);
                    }
                    return countMono.flatMap(c -> {
                        if (c > 5) {
                            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                            return exchange.getResponse().setComplete();
                        }
                        return chain.filter(exchange);
                    });
                });
    }
}

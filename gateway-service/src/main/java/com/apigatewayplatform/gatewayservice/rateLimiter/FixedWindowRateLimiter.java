package com.apigatewayplatform.gatewayservice.rateLimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class FixedWindowRateLimiter implements RateLimiter {

    private final ReactiveStringRedisTemplate redisTemplate;

    private static final int LIMIT = 5;
    private static final Duration WINDOW = Duration.ofSeconds(10);

    @Override
    public Mono<Boolean> isAllowed(String username) {

        String redisKey = "fixed:" + username;

        return redisTemplate.opsForValue()
                .increment(redisKey)
                .flatMap(count -> {

                    Mono<Long> countMono = Mono.just(count);

                    if (count == 1) {
                        countMono = redisTemplate.expire(redisKey, WINDOW)
                                .thenReturn(count);
                    }

                    return countMono.map(c -> c <= LIMIT);
                });
    }

}

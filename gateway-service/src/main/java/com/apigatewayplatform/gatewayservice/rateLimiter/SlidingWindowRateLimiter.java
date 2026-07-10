package com.apigatewayplatform.gatewayservice.rateLimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SlidingWindowRateLimiter implements RateLimiter {

    private final ReactiveStringRedisTemplate redisTemplate;

    private static final int LIMIT = 5;
    private static final Duration WINDOW = Duration.ofSeconds(10);

    @Override
    public Mono<Boolean> isAllowed(String username) {

        String redisKey = "sliding:" + username;

        Long now = System.currentTimeMillis();
        long windowStart = now - WINDOW.toMillis();
        Range<Double> range = Range.closed(0.0, (double) windowStart);

        return redisTemplate.opsForZSet()
                .removeRangeByScore(redisKey, range)
                .then(redisTemplate.opsForZSet().size(redisKey))
                .flatMap(size -> {

                    if(size >= LIMIT) {
                        return Mono.just(false);
                    }

                    // Add current request timestamp
                    return redisTemplate.opsForZSet()
                            .add(redisKey,
                                    String.valueOf(now),
                                    now)
                            .thenReturn(true);
                });
    }
}

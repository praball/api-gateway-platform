package com.apigatewayplatform.gatewayservice.rateLimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenBucketRateLimiter implements RateLimiter {

    private final ReactiveStringRedisTemplate redisTemplate;

    private static final int CAPACITY = 5;
    private static final double REFILL_RATE = 0.5;

    @Override
    public Mono<Boolean> isAllowed(String username) {

        String redisKey = "bucket:" + username;

        Long now = System.currentTimeMillis();

        return redisTemplate.opsForHash()
                .entries(redisKey)
                .collectMap(
                        entry -> entry.getKey().toString(),
                        entry -> entry.getValue().toString()
                )
                .flatMap(bucket -> {

                    double tokens;
                    long lastRefill;

                    if (bucket.isEmpty()) {
                        tokens = CAPACITY;
                        lastRefill = now;
                    } else {
                        tokens = Double.parseDouble(bucket.get("tokens"));
                        lastRefill = Long.parseLong(bucket.get("lastRefill"));
                    }

                    double elapsedSeconds = (now - lastRefill) / 1000.0;
                    double refilledTokens = elapsedSeconds * REFILL_RATE;
                    tokens = Math.min(CAPACITY, tokens + refilledTokens);
                    lastRefill = now;

                    if(tokens < 1) {
                        Map<String, String> updatedBucket = new HashMap<>();
                        updatedBucket.put("tokens", String.valueOf(tokens));
                        updatedBucket.put("lastRefill", String.valueOf(lastRefill));

                        return redisTemplate.opsForHash()
                                .putAll(redisKey, updatedBucket)
                                .then(redisTemplate.expire(redisKey, Duration.ofMinutes(30)))
                                .thenReturn(false);
                    } else {
                        tokens--;
                        Map<String, String> updatedBucket = new HashMap<>();
                        updatedBucket.put("tokens", String.valueOf(tokens));
                        updatedBucket.put("lastRefill", String.valueOf(lastRefill));

                        return redisTemplate.opsForHash()
                                .putAll(redisKey, updatedBucket)
                                .then(redisTemplate.expire(redisKey, Duration.ofMinutes(30)))
                                .thenReturn(true);
                    }
                });
    }
}

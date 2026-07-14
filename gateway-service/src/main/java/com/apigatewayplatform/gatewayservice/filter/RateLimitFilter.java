package com.apigatewayplatform.gatewayservice.filter;

import com.apigatewayplatform.gatewayservice.dto.AuditEvent;
import com.apigatewayplatform.gatewayservice.kafka.AuditProducer;
import com.apigatewayplatform.gatewayservice.rateLimiter.RateLimiter;
import com.apigatewayplatform.gatewayservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RequiredArgsConstructor
@Component
public class RateLimitFilter implements WebFilter {

    private final RateLimiter rateLimiter;

    private final JwtUtil jwtUtil;

    private final AuditProducer auditProducer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/auth")) return chain.filter(exchange);

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        return rateLimiter.isAllowed(username)
                .flatMap(allowed -> {

                    if (!allowed) {
                        exchange.getResponse()
                                .setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

                        return exchange.getResponse().setComplete();
                    }

                    AuditEvent event = AuditEvent.builder()
                            .username(username)
                            .method(exchange.getRequest().getMethod().name())
                            .path(exchange.getRequest().getURI().getPath())
                            .timestamp(Instant.now())
                            .build();

                    auditProducer.publish(event);

                    return chain.filter(exchange);
                });
    }
}

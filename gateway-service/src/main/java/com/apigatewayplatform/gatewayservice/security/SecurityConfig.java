package com.apigatewayplatform.gatewayservice.security;

import com.apigatewayplatform.gatewayservice.filter.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RateLimitFilter rateLimitFilter(
            ReactiveStringRedisTemplate redisTemplate,
            JwtUtil jwtUtil) {
        return new RateLimitFilter(redisTemplate, jwtUtil);
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http,
                                                      RateLimitFilter rateLimitFilter) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers(HttpMethod.DELETE, "/api/users/**")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.GET, "/api/users/**")
                        .hasAnyRole("ADMIN", "USER")

                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAfter(rateLimitFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}

package com.apigatewayplatform.orderservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClient.Builder webClientBuilder;

    public boolean userExists(Long userId) {
        try {
             webClientBuilder.build()
                    .get()
                    .uri("http://user-service/api/users/{id}", userId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return true;
        } catch  (Exception ex) {
            return false;
        }
    }
}

package com.apigatewayplatform.orderservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
        } catch (WebClientResponseException.NotFound ex) {
            // user doesnt exist in userdb
            return false;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to communicate with User Service", ex);
        }
    }
}

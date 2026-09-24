package com.khaaliroom.auth.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Value("${internal.service.secret}")
    private String internalServiceSecret;

    public void createProfile(UUID userId) {

        restClientBuilder
                .baseUrl(userServiceUrl)
                .build()
                .post()
                .uri("/api/v1/internal/users/{userId}/profile", userId)
                .header(
                        "X-Internal-Service-Secret",
                        internalServiceSecret
                )
                .retrieve()
                .toBodilessEntity();
    }
}
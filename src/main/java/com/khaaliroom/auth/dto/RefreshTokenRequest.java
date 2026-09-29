package com.khaaliroom.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @Schema(
                description = "Valid refresh token used to generate a new access token",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        @NotBlank(message = "Refresh token is required")
        String refreshToken

) {
}
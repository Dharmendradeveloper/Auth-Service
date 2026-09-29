package com.khaaliroom.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(

        @Schema(
                description = "JWT access token used to authenticate API requests",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        String accessToken,

        @Schema(
                description = "Refresh token used to obtain a new access token",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        String refreshToken,

        @Schema(
                description = "Authentication token type",
                example = "Bearer"
        )
        String tokenType
) {
}
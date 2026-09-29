package com.khaaliroom.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(

        @Schema(
                description = "Refresh token to be revoked during logout",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        @NotBlank(message = "Refresh token is required")
        String refreshToken

) {
}
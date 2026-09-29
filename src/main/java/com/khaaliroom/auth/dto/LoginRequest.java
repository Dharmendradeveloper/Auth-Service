package com.khaaliroom.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Schema(
                description = "User's registered email address",
                example = "rahul@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(
                description = "User's account password",
                example = "Password@123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Password is required")
        String password
) {
}
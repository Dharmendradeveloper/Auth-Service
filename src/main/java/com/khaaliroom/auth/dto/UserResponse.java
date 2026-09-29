package com.khaaliroom.auth.dto;

import com.khaaliroom.auth.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(

        @Schema(
                description = "Unique identifier of the user",
                example = "1957485e-3283-441f-8b84-465970b769aa"
        )
        UUID id,

        @Schema(
                description = "User's first name",
                example = "Rahul"
        )
        String firstName,

        @Schema(
                description = "User's last name",
                example = "Sharma"
        )
        String lastName,

        @Schema(
                description = "User's registered email address",
                example = "rahul@example.com"
        )
        String email,

        @Schema(
                description = "User's Indian mobile phone number",
                example = "9876543211"
        )
        String phone,

        @Schema(
                description = "User's role in KhaaliRoom",
                example = "OWNER"
        )
        Role role,

        @Schema(
                description = "Date and time when the user account was created",
                example = "2026-09-21T10:40:34.256936"
        )
        LocalDateTime createdAt
) {
}
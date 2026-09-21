package com.khaaliroom.auth.dto;

import com.khaaliroom.auth.entity.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Role role,
        LocalDateTime createdAt
) {
}
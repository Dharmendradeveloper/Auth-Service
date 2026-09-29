package com.khaaliroom.auth.dto;

import com.khaaliroom.auth.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(

        @Schema(
                description = "User's first name",
                example = "Rahul",
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        String firstName,

        @Schema(
                description = "User's last name",
                example = "Sharma",
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        @Schema(
                description = "User's email address",
                example = "rahul@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(
                description = "Password for the user account. Must contain between 8 and 100 characters.",
                example = "Password@123",
                minLength = 8,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        String password,

        @Schema(
                description = "Indian mobile phone number",
                example = "9876543211",
                pattern = "^[6-9]\\d{9}$",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Invalid Indian mobile number"
        )
        String phone,

        @Schema(
                description = "User's role in KhaaliRoom",
                example = "STUDENT",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Role is required")
        Role role
) {
}
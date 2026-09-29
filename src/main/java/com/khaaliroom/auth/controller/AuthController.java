package com.khaaliroom.auth.controller;

import com.khaaliroom.auth.dto.*;
import com.khaaliroom.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Authentication and user account operations"
)
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new KhaaliRoom user account."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or email already registered"
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        UserResponse response = authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns an access token and refresh token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid email or password"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get current user",
            description = "Returns the profile of the currently authenticated user."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current user returned successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @Parameter(hidden = true)
            Authentication authentication
    ) {

        UserResponse response =
                authService.getCurrentUser(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Access token refreshed successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid, expired, or revoked refresh token"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        LoginResponse response =
                authService.refreshAccessToken(
                        request.refreshToken()
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Logout",
            description = "Revokes the supplied refresh token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Logout successful"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid or already revoked refresh token"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody LogoutRequest request
    ) {

        authService.logout(request.refreshToken());

        return ResponseEntity.noContent().build();
    }
}
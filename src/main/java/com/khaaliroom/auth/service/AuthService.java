package com.khaaliroom.auth.service;

import com.khaaliroom.auth.client.UserServiceClient;
import com.khaaliroom.auth.dto.LoginRequest;
import com.khaaliroom.auth.dto.LoginResponse;
import com.khaaliroom.auth.dto.SignupRequest;
import com.khaaliroom.auth.dto.UserResponse;
import com.khaaliroom.auth.entity.RefreshToken;
import com.khaaliroom.auth.entity.User;
import com.khaaliroom.auth.exception.DuplicateResourceException;
import com.khaaliroom.auth.exception.InvalidCredentialsException;
import com.khaaliroom.auth.exception.ResourceNotFoundException;
import com.khaaliroom.auth.repository.RefreshTokenRepository;
import com.khaaliroom.auth.repository.UserRepository;
import com.khaaliroom.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserServiceClient userServiceClient;

    public UserResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(
                request.email().toLowerCase()
        )) {
            throw new DuplicateResourceException(
                    "Email is already registered"
            );
        }

        if (userRepository.existsByPhone(request.phone())) {
            throw new DuplicateResourceException(
                    "Phone number is already registered"
            );
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email().toLowerCase())
                .password(
                        passwordEncoder.encode(request.password())
                )
                .phone(request.phone())
                .role(request.role())
                .build();

        User savedUser = userRepository.save(user);
        userServiceClient.createProfile(savedUser.getId());

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.email().toLowerCase())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String accessToken =
                jwtService.generateToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );
    }

    public LoginResponse refreshAccessToken(
            String refreshTokenValue
    ) {

        RefreshToken oldRefreshToken =
                refreshTokenService.findByToken(
                        refreshTokenValue
                );

        refreshTokenService.verifyExpiration(oldRefreshToken);

        User user = oldRefreshToken.getUser();

        // Delete old refresh token
        refreshTokenRepository.delete(oldRefreshToken);

        // Create a new refresh token
        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        // Create a new access token
        String newAccessToken =
                jwtService.generateToken(user);

        return new LoginResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                "Bearer"
        );
    }

    public void logout(String refreshToken) {
        refreshTokenService.revokeToken(refreshToken);
    }

    public UserResponse getCurrentUser(String userId) {

        User user = userRepository
                .findById(UUID.fromString(userId))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
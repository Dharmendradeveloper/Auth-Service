package com.khaaliroom.auth.service;

import com.khaaliroom.auth.entity.RefreshToken;
import com.khaaliroom.auth.entity.User;
import com.khaaliroom.auth.exception.InvalidRefreshTokenException;
import com.khaaliroom.auth.repository.RefreshTokenRepository;
import com.khaaliroom.auth.security.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(
                        LocalDateTime.now()
                                .plusMinutes(
                                        jwtProperties.refreshExpirationMinutes()
                                )
                )
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(
            RefreshToken refreshToken
    ) {

        if (refreshToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new InvalidRefreshTokenException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }

    public RefreshToken findByToken(String token) {

        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException(
                                "Invalid refresh token"
                        )
                );
    }

    @Transactional
    public void revokeToken(String token) {

        refreshTokenRepository.deleteByToken(token);
    }
}
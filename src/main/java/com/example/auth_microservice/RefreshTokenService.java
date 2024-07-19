package com.example.auth_microservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AppUserRepository appUserRepository;

    public RefreshToken createRefreshToken( String username ) {
        RefreshToken refreshToken = RefreshToken.builder()
                .appUser( appUserRepository.findByUsername( username ) )
                .token( UUID.randomUUID().toString() )
                .expiryDate( Instant.now().plusMillis( 600000 ) )
                .build();

        return refreshTokenRepository.save( refreshToken );
    }

    // todo: is here should be Optional<>?
    public Optional<RefreshToken> findByToken ( String token) {
        return refreshTokenRepository.findByToken( token );
    }

    public RefreshToken verifyExpiration( RefreshToken refreshToken ) {
        if ( refreshToken.getExpiryDate().compareTo( Instant.now() ) < 0 ) {
            refreshTokenRepository.delete( refreshToken );
            throw new RuntimeException( refreshToken.getToken() + "refresh token is expired!" );
        }
        return refreshToken;
    }
}

package com.example.auth_microservice;

import com.example.auth_microservice.dto.AuthRequestDto;
import com.example.auth_microservice.dto.JwtResponseDto;
import com.example.auth_microservice.dto.RefreshTokenRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // todo: i need the mapper for mapping DTO to Entity
    public ResponseEntity<?> authenticateAndGetToken( AuthRequestDto authRequestDto ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( authRequestDto.getUsername(), authRequestDto.getPassword() )
        );

        if ( authentication.isAuthenticated() ) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken( authRequestDto.getUsername() );
            String accessToken = jwtService.generateToken( authRequestDto.getUsername() );
            return ResponseEntity
                    .ok()
                    .header( HttpHeaders.SET_COOKIE, accessToken )
                    .header( HttpHeaders.SET_COOKIE, refreshToken.toString() )
                    .build();
        } else {
            throw new UsernameNotFoundException( "Invalid user request." );
        }
    }

    public JwtResponseDto refreshToken( RefreshTokenRequestDto refreshTokenRequestDto ) {
        return refreshTokenService.findByToken( refreshTokenRequestDto.getToken() )
                .map( refreshTokenService::verifyExpiration )
                .map( RefreshToken::getAppUser )
                .map( appUser -> {
                    String accessToken = jwtService.generateToken( appUser.getUsername() );
                    return JwtResponseDto.builder()
                            .accessToken( accessToken )
                            .refreshToken( refreshTokenRequestDto.getToken() )
                            .build();
                } ).orElseThrow( () -> new RuntimeException( "Refresh Token is not in the DB!" ) );
    }

    public void saveNewUser( AuthRequestDto authRequestDto ) {
        AppUser newUser = new AppUser(
                null,
                authRequestDto.getUsername(),
                passwordEncoder.encode(authRequestDto.getPassword()),
                null );

        appUserRepository.save( newUser );
    }
}

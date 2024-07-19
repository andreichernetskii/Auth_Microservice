package com.example.auth_microservice;

import com.example.auth_microservice.dto.AuthRequestDto;
import com.example.auth_microservice.dto.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/v1" )
public class AuthController {
    private final AuthService authService;

    @PostMapping( "/login" )
    public ResponseEntity<?> authenticateAndGetToken( @RequestBody AuthRequestDto authRequestDto ) {
        return authService.authenticateAndGetToken( authRequestDto );
    }

    @PostMapping( "/save" )
    public void saveNewUser( @RequestBody AuthRequestDto authRequestDto ) {
        authService.saveNewUser( authRequestDto );
    }
}

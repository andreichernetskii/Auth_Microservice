package com.example.auth_microservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query( """
            SELECT refreshToken
            FROM RefreshToken refreshToken
            WHERE refreshToken.token = :token
            """)
    Optional<RefreshToken> findByToken( @Param( "token" ) String token );
}

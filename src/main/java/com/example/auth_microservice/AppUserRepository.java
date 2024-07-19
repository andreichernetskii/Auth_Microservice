package com.example.auth_microservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Query( """
            SELECT user
            FROM AppUser user
            WHERE user.username = :username
            """)
    AppUser findByUsername( @Param( "username" ) String username );
}

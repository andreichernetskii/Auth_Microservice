package com.example.auth_microservice;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Responsible for loading user data from DB.
 * Creates a UserDetails object for authentication purposes.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername( username );

        if ( user == null ) {
            throw new UsernameNotFoundException( "User doesn't exist!" );
        }

        return new CustomAppUserDetails( user );
    }
}

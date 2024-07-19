package com.example.auth_microservice;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal( HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain ) throws ServletException, IOException {

        String authHeader = request.getHeader( "Authorization" );
        String token = null;
        String username = null;

        if ( authHeader != null && authHeader.startsWith( "Bearer " ) ) {
            token = authHeader.substring( 7 );
            username = jwtService.extractUsername( token );
        }

        if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername( username );

            if ( jwtService.validateToken( token, userDetails ) ) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities() );
                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
                // the authenticated user stored in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication( authToken );
            }
        }

        filterChain.doFilter( request, response );
    }
}

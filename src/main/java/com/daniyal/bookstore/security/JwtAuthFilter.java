package com.daniyal.bookstore.security;

import com.daniyal.bookstore.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


public class JwtAuthFilter extends OncePerRequestFilter {


    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthFilter(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // Extract JWT token from header if it starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                // Your method to extract username (validates token as well)
                username = jwtUtil.extractUsername(jwtToken);

                // Continue with Spring Security context setup here
                // e.g. load user, set authentication in SecurityContextHolder

            }catch (NegativeArraySizeException exception)
            {
                handlerExceptionResolver.resolveException(request, response, null, exception);
                return; // Stop filter chain here, response handled above
            } catch (ExpiredJwtException | MalformedJwtException | SignatureException
                     | UnsupportedJwtException | IllegalArgumentException ex) {
                // Delegate JWT exceptions to HandlerExceptionResolver
                handlerExceptionResolver.resolveException(request, response, null, ex);
                return; // Stop filter chain here, response handled above
            }catch (Exception exception)
            {
                handlerExceptionResolver.resolveException(request, response, null, exception);
                return;
            }
        }

        // If username is found and no auth is set yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token with user details (checks signature, expiration, username)
            if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {
                // Create authentication token and set details
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}

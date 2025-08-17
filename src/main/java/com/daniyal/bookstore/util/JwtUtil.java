package com.daniyal.bookstore.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtil {


    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    private final long JWT_EXPIRATION = 1000 * 60 * 60 * 10; // 10 hour

    @PostConstruct
    public void init() {
        // Generate a secure random key. In production, use a fixed secret key loaded securely!
        // In production, load a secret from a config file or environment variable!
        this.key = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(secretKey));
    }

    // Generate token including email & roles in claims
    // Generate token - roles as list for JSON arrays
    public String generateToken(String email, Set<String> roles) {
        Map<String, Object> claims = Map.of("roles", List.copyOf(roles));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}

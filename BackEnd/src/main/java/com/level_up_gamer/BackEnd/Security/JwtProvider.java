package com.level_up_gamer.BackEnd.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtProvider {
    
    @Value("${app.jwtSecret:miClaveSecretaParaJWTDelProyectoLevelUpGamer2025}")
    private String jwtSecret;
    
    @Value("${app.jwtExpirationMs:86400000}")
    private int jwtExpirationMs; // 24 horas
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    public String generateToken(Long usuarioId, String email) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtExpirationMs / 1000, ChronoUnit.SECONDS);
        
        return Jwts.builder()
                .subject(email)
                .claim("usuarioId", usuarioId)
                .claim("email", email)
                .issuedAt(java.util.Date.from(now))
                .expiration(java.util.Date.from(expiresAt))
                .signWith(getSigningKey())
                .compact();
    }
    
    public String generateToken(Long usuarioId, String email, String rol) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtExpirationMs / 1000, ChronoUnit.SECONDS);
        
        return Jwts.builder()
                .subject(email)
                .claim("usuarioId", usuarioId)
                .claim("email", email)
                .claim("rol", rol)
                .issuedAt(java.util.Date.from(now))
                .expiration(java.util.Date.from(expiresAt))
                .signWith(getSigningKey())
                .compact();
    }
    
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    public Long getUsuarioIdFromToken(String token) {
        Object usuarioId = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("usuarioId");
        return usuarioId != null ? ((Number) usuarioId).longValue() : null;
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

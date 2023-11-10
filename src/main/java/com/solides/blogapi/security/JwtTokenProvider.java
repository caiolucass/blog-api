package com.solides.blogapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String SECRET_KEY = "46294A404E635266556A586E3272357538782F4125442A472D4B615064536756";

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder().build()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(authToken).getBody();
            return true;
        } catch (MalformedJwtException ex) {
            LOGGER.error("JWT token inválido.");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT token expirado.");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT token não suportado.");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty");
        }
        return false;
    }
}

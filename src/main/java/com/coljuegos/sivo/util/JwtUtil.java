package com.coljuegos.sivo.util;

import com.coljuegos.sivo.data.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private long jwtExpiresMinutes;

    public String extractUsername(String token) {
        Claims claims = this.validateToken(token);
        return claims.getSubject();
    }

    public Long extractPerCodigo(String token) {
        Claims claims = this.validateToken(token);
        return claims.get("perCodigo", Long.class);
    }

    public Collection<String> extractRoles(String token) {
        Claims claims = this.validateToken(token);
        List<?> rawRoles = claims.get("roles", List.class);

        if (rawRoles == null) return List.of();

        return rawRoles.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toList();
    }

    public Claims validateToken(String token) throws JwtException {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch(JwtException e){
            throw new JwtException(e.getMessage());
        }
    }

    public SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDTO user){
        return Jwts.builder()
                .subject(user.getIdUser())
                .claim("email", user.getEmail())
                .claim("identificacion", user.getNumIdentificacion())
                .claim("perCodigo", user.getPerCodigo())
                .claim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiresMinutes * 60 * 1000))
                .signWith(getSignInKey())
                .compact();
    }

}

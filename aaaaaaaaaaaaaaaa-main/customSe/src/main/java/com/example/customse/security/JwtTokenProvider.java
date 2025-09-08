package com.example.customse.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecretBase64;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationInMs;
    private SecretKey getSigningKey() {
//        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecretBase64);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    //generate token
    public String generateToken(Authentication authentication) {
        String username=authentication.getName();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + jwtExpirationInMs);
        SecretKey key = getSigningKey();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }
    //get username from the token
    public String getUsernameFromJWT(String token) {
        SecretKey key = getSigningKey();

        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    //validate JWT token

    public boolean validateToken(String token) throws RuntimeException {
        try {
            SecretKey key = getSigningKey();
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new RuntimeException();
        }
    }
}

package com.redditclone.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtTokenProvider {

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String email) {
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

        return Jwts.builder()
                .claims()
                .subject(email)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String authToken) {
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromJwt(String authToken) {
        Claims claims = Jwts.parser()
                            .verifyWith(getSigningKey())
                            .build().parseSignedClaims(authToken)
                            .getPayload();

        String email = claims.getSubject();
        return email;
    }
}

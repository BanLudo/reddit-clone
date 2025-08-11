package com.redditclone.backend.service.jwt;

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
}

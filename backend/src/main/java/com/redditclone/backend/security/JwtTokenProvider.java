package com.redditclone.backend.security;

import com.redditclone.backend.service.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtTokenProvider {

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    @Value("${jwt.secret}")
    private String secretKey;


    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

        return Jwts.builder()
                .claims()
                .subject(Long.toString(userPrincipal.getId()))
                .issuedAt(new Date())
                .expiration(expirationDate)
                .and()
                .signWith(getSigningKey())
                .compact();
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

    public Long getUserIdFromJwt(String authToken) {
        Claims claims = Jwts.parser()
                            .verifyWith(getSigningKey())
                            .build()
                            .parseSignedClaims(authToken)
                            .getPayload();

        return Long.parseLong(claims.getSubject());
    }


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

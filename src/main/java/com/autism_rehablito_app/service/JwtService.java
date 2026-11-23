package com.autism_rehablito_app.service;

import com.autism_rehablito_app.config.JwtConfig;
import com.autism_rehablito_app.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService() {

        this.secretKey =
                Keys.hmacShaKeyFor(
                        JwtConfig.SECRET_KEY
                                .getBytes(StandardCharsets.UTF_8)
                );
    }

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())

                .claim(
                        "userId",
                        user.getId()
                )

                .claim(
                        "name",
                        user.getName()
                )

                .claim(
                        "role",
                        user.getRole().name()
                )

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + JwtConfig.EXPIRATION_TIME
                        )
                )

                .signWith(secretKey)

                .compact();
    }

    public String extractEmail(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(
            String token,
            User user) {

        try {

            String email =
                    extractEmail(token);

            Date expiration =
                    extractAllClaims(token)
                            .getExpiration();

            return email.equals(user.getEmail())
                    && expiration.after(
                            new Date()
                    );

        } catch (Exception exception) {

            return false;
        }
    }
}
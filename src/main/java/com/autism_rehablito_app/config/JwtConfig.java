package com.autism_rehablito_app.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    /*
     * Development secret only.
     *
     * Production mein secret ko
     * environment variable / secure secret
     * storage se load karenge.
     */
    public static final String SECRET_KEY =
            "AutismCentreBackendJwtSecretKey2026VeryLongSecretKey";

    public static final long EXPIRATION_TIME =
            1000L * 60 * 60 * 24;
}
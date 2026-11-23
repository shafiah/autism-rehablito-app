package com.autism_rehablito_app.config;

import com.autism_rehablito_app.security.JwtAuthenticationEntryPoint;
import com.autism_rehablito_app.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // REST API ke liye CSRF disable
            .csrf(csrf -> csrf.disable())
            
         // Allow H2 console in iframe
            .headers(headers -> headers
                    .frameOptions(frame -> frame.disable())
            )

            // JWT based authentication - session nahi banegi
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            // Authentication rules
            .authorizeHttpRequests(auth -> auth
            		
            		.requestMatchers("/h2-console/**").permitAll()  // H2 console allow
            		
            		.requestMatchers("/api/enquiries", "/api/enquiries/**")
            		.hasRole("ADMIN")
            		
                    .requestMatchers("/api/health")
                    .permitAll()
                    
                    .requestMatchers(
                            org.springframework.http.HttpMethod.GET,
                            "/api/admin/branches",
                            "/api/admin/branches/**"
                    )
                    .permitAll()

                    .requestMatchers(
                            "/api/admin/auth/login",

                            "/api/parent/auth/register",
                            "/api/parent/auth/login",
                            "/api/parent/auth/forgot-password",
                            "/api/parent/auth/verify-otp",
                            "/api/parent/auth/reset-password",

                            "/api/therapist/auth/login"
                    )
                    .permitAll()
                    
                 // Temporary Razorpay test page
                   // .requestMatchers("/razorpay-test.html").permitAll()

                    .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")

                    .requestMatchers("/api/therapist/**")
                    .hasRole("THERAPIST")

                    .anyRequest()
                    .authenticated()
            )
            // Unauthorized request handling
            .exceptionHandling(exception ->
                    exception.authenticationEntryPoint(
                            jwtAuthenticationEntryPoint
                    )
            )

            // Browser login form disable
            .formLogin(form -> form.disable())

            // HTTP Basic disable
            .httpBasic(basic -> basic.disable())

            // JWT filter ko Spring Security ke
            // UsernamePasswordAuthenticationFilter se pehle run karna hai
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
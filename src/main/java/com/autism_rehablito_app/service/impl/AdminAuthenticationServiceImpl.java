package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.admin.AdminLoginRequest;
import com.autism_rehablito_app.dto.admin.AdminLoginResponse;
import com.autism_rehablito_app.entity.Role;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.repository.UserRepository;
import com.autism_rehablito_app.service.AdminAuthenticationService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.autism_rehablito_app.exception.UnauthorizedException;
import com.autism_rehablito_app.service.JwtService;

@Service
public class AdminAuthenticationServiceImpl
        implements AdminAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminAuthenticationServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminLoginResponse login(
            AdminLoginRequest request) {

        User user =
                userRepository
                        .findByEmail(request.getEmail().trim())
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Invalid email or password"
                                )
                        );

        if (user.getRole() != Role.ADMIN) {
        	throw new UnauthorizedException(
        	        "Admin access is not allowed"
        	);
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
        	throw new UnauthorizedException(
        	        "Admin account is inactive"
        	);
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

        	throw new UnauthorizedException(
        	        "Invalid email or password"
        	);
        }

        String token =
                jwtService.generateToken(user);

        return new AdminLoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }
}
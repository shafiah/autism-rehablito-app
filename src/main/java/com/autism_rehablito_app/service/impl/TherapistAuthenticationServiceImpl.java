package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.therapist.TherapistLoginRequest;
import com.autism_rehablito_app.dto.therapist.TherapistLoginResponse;
import com.autism_rehablito_app.entity.Role;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.exception.UnauthorizedException;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.UserRepository;
import com.autism_rehablito_app.service.JwtService;
import com.autism_rehablito_app.service.TherapistAuthenticationService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TherapistAuthenticationServiceImpl
        implements TherapistAuthenticationService {

    private final UserRepository userRepository;
    private final TherapistRepository therapistRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TherapistAuthenticationServiceImpl(
            UserRepository userRepository,
            TherapistRepository therapistRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.therapistRepository = therapistRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional(readOnly = true)
    public TherapistLoginResponse login(
            TherapistLoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail().trim())
                .orElseThrow(
                        () -> new UnauthorizedException(
                                "Invalid email or password"
                        )
                );

        if (user.getRole() != Role.THERAPIST) {
            throw new UnauthorizedException(
                    "Therapist access is not allowed"
            );
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UnauthorizedException(
                    "Therapist account is inactive"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new UnauthorizedException(
                    "Invalid email or password"
            );
        }

        Therapist therapist =
                therapistRepository
                        .findByUserId(user.getId())
                        .orElseThrow(
                                () -> new UnauthorizedException(
                                        "Therapist profile not found"
                                )
                        );

        if (!Boolean.TRUE.equals(
                therapist.getActive())) {

            throw new UnauthorizedException(
                    "Therapist profile is inactive"
            );
        }

        if (therapist.getBranch() == null) {
            throw new UnauthorizedException(
                    "Therapist branch is not assigned"
            );
        }

        String token =
                jwtService.generateToken(user);

        return new TherapistLoginResponse(
                user.getId(),
                therapist.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                therapist.getBranch().getId(),
                therapist.getBranch().getBranchName(),
                therapist.getTherapyType(),
                token
        );
    }
}
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.therapist.TherapistLoginRequest;
import com.autism_rehablito_app.dto.therapist.TherapistLoginResponse;
import com.autism_rehablito_app.service.TherapistAuthenticationService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/therapist/auth")
public class TherapistAuthenticationController {

    private final TherapistAuthenticationService
            therapistAuthenticationService;

    public TherapistAuthenticationController(
            TherapistAuthenticationService
                    therapistAuthenticationService) {

        this.therapistAuthenticationService =
                therapistAuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<TherapistLoginResponse> login(
            @Valid @RequestBody TherapistLoginRequest request) {

        return ResponseEntity.ok(
                therapistAuthenticationService.login(
                        request
                )
        );
    }
}
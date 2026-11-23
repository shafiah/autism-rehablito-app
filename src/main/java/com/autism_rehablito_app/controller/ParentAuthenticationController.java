package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.parent.ParentForgotPasswordRequest;
import com.autism_rehablito_app.dto.parent.ParentForgotPasswordResponse;
import com.autism_rehablito_app.dto.parent.ParentLoginRequest;
import com.autism_rehablito_app.dto.parent.ParentLoginResponse;
import com.autism_rehablito_app.dto.parent.ParentRegisterRequest;
import com.autism_rehablito_app.dto.parent.ParentRegisterResponse;
import com.autism_rehablito_app.dto.parent.ParentResetPasswordRequest;
import com.autism_rehablito_app.dto.parent.ParentVerifyOtpRequest;
import com.autism_rehablito_app.dto.parent.ParentVerifyOtpResponse;
import com.autism_rehablito_app.service.ParentAuthenticationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parent/auth")
public class ParentAuthenticationController {

    private final ParentAuthenticationService
            parentAuthenticationService;

    public ParentAuthenticationController(
            ParentAuthenticationService parentAuthenticationService) {

        this.parentAuthenticationService =
                parentAuthenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ParentRegisterResponse> register(
            @Valid @RequestBody ParentRegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        parentAuthenticationService.register(
                                request
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ParentLoginResponse> login(
            @Valid @RequestBody ParentLoginRequest request) {

        return ResponseEntity.ok(
                parentAuthenticationService.login(
                        request
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ParentForgotPasswordResponse>
    forgotPassword(
            @Valid @RequestBody
            ParentForgotPasswordRequest request) {

        return ResponseEntity.ok(
                parentAuthenticationService.forgotPassword(
                        request
                )
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ParentVerifyOtpResponse>
    verifyOtp(
            @Valid @RequestBody
            ParentVerifyOtpRequest request) {

        return ResponseEntity.ok(
                parentAuthenticationService.verifyOtp(
                        request
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody
            ParentResetPasswordRequest request) {

        parentAuthenticationService.resetPassword(
                request
        );

        return ResponseEntity.noContent().build();
    }
}
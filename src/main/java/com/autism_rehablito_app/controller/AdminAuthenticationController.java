package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminLoginRequest;
import com.autism_rehablito_app.dto.admin.AdminLoginResponse;
import com.autism_rehablito_app.service.AdminAuthenticationService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthenticationController {

    private final AdminAuthenticationService
            adminAuthenticationService;

    public AdminAuthenticationController(
            AdminAuthenticationService
                    adminAuthenticationService) {

        this.adminAuthenticationService =
                adminAuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(
            @Valid @RequestBody AdminLoginRequest request) {

        return ResponseEntity.ok(
                adminAuthenticationService.login(
                        request
                )
        );
    }
}
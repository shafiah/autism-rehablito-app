package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminSessionCreateRequest;
import com.autism_rehablito_app.dto.admin.AdminSessionResponse;
import com.autism_rehablito_app.dto.admin.AdminSessionUpdateRequest;
import com.autism_rehablito_app.service.AdminSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sessions")
public class AdminSessionController {

    private final AdminSessionService adminSessionService;

    public AdminSessionController(
            AdminSessionService adminSessionService) {

        this.adminSessionService = adminSessionService;
    }

    @PostMapping
    public ResponseEntity<AdminSessionResponse> createSession(
            @Valid @RequestBody AdminSessionCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminSessionService.createSession(request));
    }

    @GetMapping
    public ResponseEntity<List<AdminSessionResponse>> getAllSessions(
            @RequestParam(required = false) Long branchId) {

        if (branchId != null) {
            return ResponseEntity.ok(
                    adminSessionService.getSessionsByBranch(branchId)
            );
        }

        return ResponseEntity.ok(
                adminSessionService.getAllSessions()
        );
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<AdminSessionResponse> getSessionById(
            @PathVariable Long sessionId) {

        return ResponseEntity.ok(
                adminSessionService.getSessionById(sessionId)
        );
    }
    
    @PutMapping("/{sessionId}")
    public ResponseEntity<AdminSessionResponse> updateSession(
            @PathVariable Long sessionId,
            @Valid @RequestBody AdminSessionUpdateRequest request) {

        return ResponseEntity.ok(
                adminSessionService.updateSession(
                        sessionId,
                        request
                )
        );
    }
    
    @PutMapping("/{sessionId}/cancel")
    public ResponseEntity<AdminSessionResponse> cancelSession(
            @PathVariable Long sessionId) {

        return ResponseEntity.ok(
                adminSessionService.cancelSession(
                        sessionId
                )
        );
    }
}
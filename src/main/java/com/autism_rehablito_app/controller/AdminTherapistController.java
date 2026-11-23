package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminTherapistCreateRequest;
import com.autism_rehablito_app.dto.admin.AdminTherapistCreateResponse;
import com.autism_rehablito_app.dto.admin.AdminTherapistResponse;
import com.autism_rehablito_app.service.AdminTherapistService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/therapists")
public class AdminTherapistController {

    private final AdminTherapistService adminTherapistService;

    public AdminTherapistController(
            AdminTherapistService adminTherapistService) {

        this.adminTherapistService =
                adminTherapistService;
    }

    @PostMapping
    public ResponseEntity<AdminTherapistCreateResponse>
    createTherapist(
            @Valid
            @RequestBody
            AdminTherapistCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        adminTherapistService.createTherapist(
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<AdminTherapistResponse>>
    getTherapists(
            @RequestParam(required = false)
            Long branchId) {

        if (branchId != null) {

            return ResponseEntity.ok(
                    adminTherapistService
                            .getTherapistsByBranch(
                                    branchId
                            )
            );
        }

        return ResponseEntity.ok(
                adminTherapistService
                        .getAllTherapists()
        );
    }

    @GetMapping("/{therapistId}")
    public ResponseEntity<AdminTherapistResponse>
    getTherapistById(
            @PathVariable Long therapistId) {

        return ResponseEntity.ok(
                adminTherapistService
                        .getTherapistById(
                                therapistId
                        )
        );
    }
    
    @PutMapping("/{therapistId}/activate")
    public ResponseEntity<AdminTherapistResponse> activateTherapist(
            @PathVariable Long therapistId) {

        return ResponseEntity.ok(
                adminTherapistService.activateTherapist(therapistId)
        );
    }
    
    @PutMapping("/{therapistId}/deactivate")
    public ResponseEntity<AdminTherapistResponse> deactivateTherapist(
            @PathVariable Long therapistId) {

        return ResponseEntity.ok(
                adminTherapistService.deactivateTherapist(therapistId)
        );
    }
}
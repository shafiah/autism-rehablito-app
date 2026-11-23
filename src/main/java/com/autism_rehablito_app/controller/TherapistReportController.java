
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.therapist.TherapistReportCreateRequest;
import com.autism_rehablito_app.dto.therapist.TherapistReportResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.TherapistReportService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapist/reports")
public class TherapistReportController {

    private final TherapistReportService therapistReportService;

    public TherapistReportController(
            TherapistReportService therapistReportService) {

        this.therapistReportService = therapistReportService;
    }

    // =====================================================
    // CREATE DRAFT REPORT
    // POST /api/therapist/reports/children/{childId}
    // =====================================================

    @PostMapping("/children/{childId}")
    public ResponseEntity<TherapistReportResponse> createDraftReport(
            Authentication authentication,
            @PathVariable Long childId,
            @Valid @RequestBody TherapistReportCreateRequest request) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        therapistReportService.createDraftReport(
                                user.getId(),
                                childId,
                                request
                        )
                );
    }

    // =====================================================
    // GET LOGGED-IN THERAPIST'S REPORTS
    // GET /api/therapist/reports
    // =====================================================

    @GetMapping
    public ResponseEntity<List<TherapistReportResponse>> getMyReports(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistReportService.getMyReports(
                        user.getId()
                )
        );
    }

    // =====================================================
    // SUBMIT DRAFT REPORT
    // PUT /api/therapist/reports/{reportId}/submit
    // =====================================================

    @PutMapping("/{reportId}/submit")
    public ResponseEntity<TherapistReportResponse> submitReport(
            Authentication authentication,
            @PathVariable Long reportId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistReportService.submitReport(
                        user.getId(),
                        reportId
                )
        );
    }
}

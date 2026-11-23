
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminReportResponse;
import com.autism_rehablito_app.dto.admin.AdminReportReviewRequest;
import com.autism_rehablito_app.entity.ReportStatus;
import com.autism_rehablito_app.service.AdminReportService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    private final AdminReportService adminReportService;

    public AdminReportController(
            AdminReportService adminReportService) {

        this.adminReportService = adminReportService;
    }

    @GetMapping
    public ResponseEntity<List<AdminReportResponse>> getReportsByStatus(
            @RequestParam ReportStatus status) {

        return ResponseEntity.ok(
                adminReportService.getReportsByStatus(status)
        );
    }

    @PutMapping("/{reportId}/review")
    public ResponseEntity<AdminReportResponse> reviewReport(
            @PathVariable Long reportId,
            @Valid @RequestBody AdminReportReviewRequest request) {

        return ResponseEntity.ok(
                adminReportService.reviewReport(
                        reportId,
                        request
                )
        );
    }

    @PutMapping("/{reportId}/publish")
    public ResponseEntity<AdminReportResponse> publishReport(
            @PathVariable Long reportId) {

        return ResponseEntity.ok(
                adminReportService.publishReport(reportId)
        );
    }
}

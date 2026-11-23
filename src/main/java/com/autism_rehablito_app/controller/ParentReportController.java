package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.parent.ParentReportResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.ParentReportService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent/children/{childId}/reports")
public class ParentReportController {

    private final ParentReportService parentReportService;

    public ParentReportController(
            ParentReportService parentReportService) {

        this.parentReportService = parentReportService;
    }

    @GetMapping
    public ResponseEntity<List<ParentReportResponse>> getMyChildReports(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentReportService.getMyChildReports(
                        user.getId(),
                        childId
                )
        );
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ParentReportResponse> getMyChildReportById(
            Authentication authentication,
            @PathVariable Long childId,
            @PathVariable Long reportId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentReportService.getMyChildReportById(
                        user.getId(),
                        childId,
                        reportId
                )
        );
    }
}
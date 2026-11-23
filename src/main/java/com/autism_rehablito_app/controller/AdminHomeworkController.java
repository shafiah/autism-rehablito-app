package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminHomeworkResponse;
import com.autism_rehablito_app.service.AdminHomeworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/homework")
public class AdminHomeworkController {

    private final AdminHomeworkService adminHomeworkService;

    public AdminHomeworkController(
            AdminHomeworkService adminHomeworkService) {
        this.adminHomeworkService = adminHomeworkService;
    }

    /**
     * Get homework records with optional filters.
     *
     * Example:
     * GET /api/admin/homework
     * GET /api/admin/homework?branchId=1
     * GET /api/admin/homework?branchId=1&childId=5
     * GET /api/admin/homework?status=ASSIGNED
     * GET /api/admin/homework?therapyType=Speech%20Therapy
     */
    @GetMapping
    public ResponseEntity<List<AdminHomeworkResponse>> getHomework(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long childId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String therapyType) {

        List<AdminHomeworkResponse> homeworkList =
                adminHomeworkService.getHomework(
                        branchId,
                        childId,
                        status,
                        therapyType
                );

        return ResponseEntity.ok(homeworkList);
    }

    /**
     * Get one homework record by its ID.
     *
     * Example:
     * GET /api/admin/homework/1
     */
    @GetMapping("/{homeworkId}")
    public ResponseEntity<AdminHomeworkResponse> getHomeworkById(
            @PathVariable Long homeworkId) {

        AdminHomeworkResponse response =
                adminHomeworkService.getHomeworkById(homeworkId);

        return ResponseEntity.ok(response);
    }
}

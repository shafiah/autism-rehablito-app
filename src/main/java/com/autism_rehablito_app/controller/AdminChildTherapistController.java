package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminChildTherapistResponse;
import com.autism_rehablito_app.service.AdminChildTherapistService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/child-therapists")
public class AdminChildTherapistController {

    private final AdminChildTherapistService
            adminChildTherapistService;

    public AdminChildTherapistController(
            AdminChildTherapistService adminChildTherapistService) {

        this.adminChildTherapistService =
                adminChildTherapistService;
    }

    @PostMapping(
            "/children/{childId}/therapists/{therapistId}"
    )
    public ResponseEntity<AdminChildTherapistResponse>
    assignTherapist(
            @PathVariable Long childId,
            @PathVariable Long therapistId) {

        return ResponseEntity.ok(
                adminChildTherapistService.assignTherapist(
                        childId,
                        therapistId
                )
        );
    }

    @GetMapping("/children/{childId}/therapists")
    public ResponseEntity<List<AdminChildTherapistResponse>>
    getTherapistsByChild(
            @PathVariable Long childId) {

        return ResponseEntity.ok(
                adminChildTherapistService
                        .getTherapistsByChild(childId)
        );
    }

    @GetMapping("/therapists/{therapistId}/children")
    public ResponseEntity<List<AdminChildTherapistResponse>>
    getChildrenByTherapist(
            @PathVariable Long therapistId) {

        return ResponseEntity.ok(
                adminChildTherapistService
                        .getChildrenByTherapist(therapistId)
        );
    }

    @PutMapping("/{assignmentId}/unassign")
    public ResponseEntity<AdminChildTherapistResponse>
    unassignTherapist(
            @PathVariable Long assignmentId) {

        return ResponseEntity.ok(
                adminChildTherapistService
                        .unassignTherapist(assignmentId)
        );
    }
}
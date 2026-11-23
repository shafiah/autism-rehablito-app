
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.ProgressRecordResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.ProgressRecordService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/parent/children/{childId}/progress")
public class ProgressRecordController {

    private final ProgressRecordService progressRecordService;

    public ProgressRecordController(
            ProgressRecordService progressRecordService) {
        this.progressRecordService = progressRecordService;
    }

    @GetMapping
    public ResponseEntity<List<ProgressRecordResponse>> getMyChildProgress(
            @PathVariable Long childId,
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof User)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Valid authentication required");
        }

        User user = (User) authentication.getPrincipal();

        if (user.getId() == null
                || user.getRole() == null
                || !user.getRole().name().equals("PARENT")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Parent access required");
        }

        List<ProgressRecordResponse> progressRecords =
                progressRecordService.getMyChildProgress(
                        user.getId(),
                        childId);

        return ResponseEntity.ok(progressRecords);
    }
}

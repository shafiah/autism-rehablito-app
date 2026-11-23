
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.therapist.TherapistProgressRequest;
import com.autism_rehablito_app.dto.therapist.TherapistProgressResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.TherapistProgressService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapist/progress")
public class TherapistProgressController {

    private final TherapistProgressService therapistProgressService;

    public TherapistProgressController(
            TherapistProgressService therapistProgressService) {

        this.therapistProgressService =
                therapistProgressService;
    }

    @PostMapping("/sessions/{sessionId}")
    public ResponseEntity<TherapistProgressResponse> createProgress(
            Authentication authentication,
            @PathVariable Long sessionId,
            @Valid @RequestBody TherapistProgressRequest request) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        therapistProgressService.createProgress(
                                user.getId(),
                                sessionId,
                                request
                        )
                );
    }

    @GetMapping("/children/{childId}")
    public ResponseEntity<List<TherapistProgressResponse>> getChildProgress(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistProgressService.getChildProgress(
                        user.getId(),
                        childId
                )
        );
    }
}

package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.therapist.TherapistSessionResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.TherapistSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapist/sessions")
public class TherapistSessionController {

    private final TherapistSessionService therapistSessionService;

    public TherapistSessionController(
            TherapistSessionService therapistSessionService) {

        this.therapistSessionService =
                therapistSessionService;
    }

    @GetMapping
    public ResponseEntity<List<TherapistSessionResponse>>
    getMySessions(Authentication authentication) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistSessionService
                        .getMySessions(user.getId())
        );
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<TherapistSessionResponse>
    getMySessionById(
            Authentication authentication,
            @PathVariable Long sessionId) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistSessionService
                        .getMySessionById(
                                user.getId(),
                                sessionId
                        )
        );
    }

    @PutMapping("/{sessionId}/start")
    public ResponseEntity<TherapistSessionResponse>
    startSession(
            Authentication authentication,
            @PathVariable Long sessionId) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistSessionService
                        .startSession(
                                user.getId(),
                                sessionId
                        )
        );
    }

    @PutMapping("/{sessionId}/complete")
    public ResponseEntity<TherapistSessionResponse>
    completeSession(
            Authentication authentication,
            @PathVariable Long sessionId) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistSessionService
                        .completeSession(
                                user.getId(),
                                sessionId
                        )
        );
    }
}
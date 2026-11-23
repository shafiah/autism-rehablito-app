package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.parent.ParentSessionResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.ParentSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent/children")
public class ParentSessionController {

    private final ParentSessionService parentSessionService;

    public ParentSessionController(
            ParentSessionService parentSessionService) {

        this.parentSessionService = parentSessionService;
    }

    @GetMapping("/{childId}/sessions")
    public ResponseEntity<List<ParentSessionResponse>>
    getMyChildSessions(
            Authentication authentication,
            @PathVariable Long childId) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentSessionService.getMyChildSessions(
                        user.getId(),
                        childId
                )
        );
    }

    @GetMapping("/{childId}/sessions/{sessionId}")
    public ResponseEntity<ParentSessionResponse>
    getMyChildSessionById(
            Authentication authentication,
            @PathVariable Long childId,
            @PathVariable Long sessionId) {

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentSessionService.getMyChildSessionById(
                        user.getId(),
                        childId,
                        sessionId
                )
        );
    }
}
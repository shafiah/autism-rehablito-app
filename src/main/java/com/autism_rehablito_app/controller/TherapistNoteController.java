
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.therapist.TherapistNoteRequest;
import com.autism_rehablito_app.dto.therapist.TherapistNoteResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.TherapistNoteService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapist/notes")
public class TherapistNoteController {

    private final TherapistNoteService therapistNoteService;

    public TherapistNoteController(
            TherapistNoteService therapistNoteService) {

        this.therapistNoteService = therapistNoteService;
    }

    // Save a therapist note for a session
    @PostMapping("/sessions/{sessionId}")
    public ResponseEntity<TherapistNoteResponse> createNote(
            Authentication authentication,
            @PathVariable Long sessionId,
            @Valid @RequestBody TherapistNoteRequest request) {

        User user = (User) authentication.getPrincipal();

        TherapistNoteResponse response =
                therapistNoteService.createNote(
                        user.getId(),
                        sessionId,
                        request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get notes for a child belonging to the logged-in therapist
    @GetMapping("/children/{childId}")
    public ResponseEntity<List<TherapistNoteResponse>> getChildNotes(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistNoteService.getChildNotes(
                        user.getId(),
                        childId));
    }
}

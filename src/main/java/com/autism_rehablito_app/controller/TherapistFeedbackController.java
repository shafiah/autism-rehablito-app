
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.therapist.TherapistFeedbackResponse;
import com.autism_rehablito_app.entity.Feedback;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.FeedbackService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/therapist/feedback")
public class TherapistFeedbackController {

    private final FeedbackService feedbackService;

    public TherapistFeedbackController(
            FeedbackService feedbackService
    ) {
        this.feedbackService = feedbackService;
    }

    /*
     * Get only published feedback of logged-in therapist.
     */
    @GetMapping
    public ResponseEntity<List<TherapistFeedbackResponse>> getPublishedFeedback(
            Authentication authentication
    ) {

        User therapist = getAuthenticatedTherapist(authentication);

        List<TherapistFeedbackResponse> response =
                feedbackService
                        .getPublishedFeedbackForTherapist(therapist.getId())
                        .stream()
                        .map(TherapistFeedbackResponse::new)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /*
     * Verify authenticated Therapist.
     */
    private User getAuthenticatedTherapist(
            Authentication authentication
    ) {

        if (authentication == null
                || authentication.getPrincipal() == null) {
            throw new AccessDeniedException(
                    "Authentication is required."
            );
        }

        User therapist = (User) authentication.getPrincipal();

        if (therapist.getRole() == null
                || !therapist.getRole().name().equals("THERAPIST")) {
            throw new AccessDeniedException(
                    "Only Therapist can access published feedback."
            );
        }

        return therapist;
    }
}

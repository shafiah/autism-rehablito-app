
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminFeedbackResponse;
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
@RequestMapping("/api/admin/feedback")
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    public AdminFeedbackController(
            FeedbackService feedbackService
    ) {
        this.feedbackService = feedbackService;
    }

    /*
     * Get all parent feedback records.
     */
    @GetMapping
    public ResponseEntity<List<AdminFeedbackResponse>> getAllFeedback(
            Authentication authentication
    ) {

        User admin = getAuthenticatedAdmin(authentication);

        List<AdminFeedbackResponse> response =
                feedbackService.getAllFeedback()
                        .stream()
                        .map(AdminFeedbackResponse::new)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /*
     * Publish an individual feedback record.
     */
    @PutMapping("/{feedbackId}/publish")
    public ResponseEntity<AdminFeedbackResponse> publishFeedback(
            @PathVariable Long feedbackId,
            Authentication authentication
    ) {

        User admin = getAuthenticatedAdmin(authentication);

        Feedback feedback =
                feedbackService.publishFeedback(feedbackId);

        return ResponseEntity.ok(
                new AdminFeedbackResponse(feedback)
        );
    }

    /*
     * Verify authenticated Admin.
     */
    private User getAuthenticatedAdmin(
            Authentication authentication
    ) {

        if (authentication == null
                || authentication.getPrincipal() == null) {
            throw new AccessDeniedException(
                    "Authentication is required."
            );
        }

        User admin = (User) authentication.getPrincipal();

        if (admin.getRole() == null
                || !admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException(
                    "Only Admin can access feedback management."
            );
        }

        return admin;
    }
}

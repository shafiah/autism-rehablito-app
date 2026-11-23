
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.parent.ParentFeedbackRequest;
import com.autism_rehablito_app.dto.parent.ParentFeedbackResponse;
import com.autism_rehablito_app.entity.Feedback;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.FeedbackService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parent/feedback")
public class ParentFeedbackController {

    private final FeedbackService feedbackService;

    public ParentFeedbackController(
            FeedbackService feedbackService
    ) {
        this.feedbackService = feedbackService;
    }

    /*
     * Submit feedback for an assigned therapist.
     */
    @PostMapping
    public ResponseEntity<ParentFeedbackResponse> submitFeedback(
            @RequestBody ParentFeedbackRequest request,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        Feedback feedback = feedbackService.submitFeedback(
                user.getId(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ParentFeedbackResponse(feedback));
    }

    /*
     * Get feedback history of logged-in parent.
     */
    @GetMapping
    public ResponseEntity<List<ParentFeedbackResponse>> getFeedbackHistory(
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        List<ParentFeedbackResponse> response =
                feedbackService.getParentFeedbackHistory(user.getId())
                        .stream()
                        .map(ParentFeedbackResponse::new)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /*
     * Authentication principal is the actual User entity.
     */
    private User getAuthenticatedUser(
            Authentication authentication
    ) {

        return (User) authentication.getPrincipal();
    }
}

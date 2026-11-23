package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.therapist.TherapistMessageReadResponse;
import com.autism_rehablito_app.dto.therapist.TherapistMessageRequest;
import com.autism_rehablito_app.dto.therapist.TherapistMessageResponse;
import com.autism_rehablito_app.dto.therapist.TherapistUnreadMessageResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.TherapistCommunicationService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/therapist/communication")
public class TherapistCommunicationController {

    private final TherapistCommunicationService therapistCommunicationService;

    public TherapistCommunicationController(
            TherapistCommunicationService therapistCommunicationService) {

        this.therapistCommunicationService =
                therapistCommunicationService;
    }

    @PostMapping("/children/{childId}/messages")
    public ResponseEntity<TherapistMessageResponse> sendMessage(
            Authentication authentication,
            @PathVariable Long childId,
            @Valid @RequestBody TherapistMessageRequest request) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistCommunicationService.sendMessage(
                        user.getId(),
                        childId,
                        request
                )
        );
    }
    
    @GetMapping("/children/{childId}/messages")
    public ResponseEntity<List<TherapistMessageResponse>> getConversation(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistCommunicationService.getConversation(
                        user.getId(),
                        childId
                )
        );
    }
    
    @PutMapping(
            "/children/{childId}/messages/{messageId}/read"
    )
    public ResponseEntity<TherapistMessageReadResponse> markMessageAsRead(
            Authentication authentication,
            @PathVariable Long childId,
            @PathVariable Long messageId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistCommunicationService.markMessageAsRead(
                        user.getId(),
                        childId,
                        messageId
                )
        );
    }
    
    @GetMapping(
            "/children/{childId}/messages/unread-count"
    )
    public ResponseEntity<TherapistUnreadMessageResponse> getUnreadCount(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistCommunicationService.getUnreadCount(
                        user.getId(),
                        childId
                )
        );
    }
}
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.parent.ParentMessageReadResponse;
import com.autism_rehablito_app.dto.parent.ParentMessageRequest;
import com.autism_rehablito_app.dto.parent.ParentMessageResponse;
import com.autism_rehablito_app.dto.parent.ParentUnreadMessageResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.ParentCommunicationService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parent/communication")
public class ParentCommunicationController {

    private final ParentCommunicationService parentCommunicationService;

    public ParentCommunicationController(
            ParentCommunicationService parentCommunicationService) {

        this.parentCommunicationService =
                parentCommunicationService;
    }

    @PostMapping("/children/{childId}/therapists/{therapistId}/messages")
    public ResponseEntity<ParentMessageResponse> sendMessage(
            Authentication authentication,
            @PathVariable Long childId,
            @PathVariable Long therapistId,
            @Valid @RequestBody ParentMessageRequest request) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentCommunicationService.sendMessage(
                        user.getId(),
                        childId,
                        therapistId,
                        request
                )
        );
    }
    
    @GetMapping("/children/{childId}/therapists/{therapistId}/messages")
    public ResponseEntity<List<ParentMessageResponse>> getConversation(
            Authentication authentication,
            @PathVariable Long childId,
            @PathVariable Long therapistId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentCommunicationService.getConversation(
                        user.getId(),
                        childId,
                        therapistId
                )
        );
    }
    
    @PutMapping(
            "/children/{childId}/therapists/{therapistId}/messages/{messageId}/read"
    )
    public ResponseEntity<ParentMessageReadResponse> markMessageAsRead(
            Authentication authentication,
            @PathVariable Long childId,
            @PathVariable Long therapistId,
            @PathVariable Long messageId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentCommunicationService.markMessageAsRead(
                        user.getId(),
                        childId,
                        therapistId,
                        messageId
                )
        );
    }
    
    @GetMapping(
            "/children/{childId}/therapists/{therapistId}/messages/unread-count"
    )
    public ResponseEntity<ParentUnreadMessageResponse> getUnreadCount(
            Authentication authentication,
            @PathVariable Long childId,
            @PathVariable Long therapistId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentCommunicationService.getUnreadCount(
                        user.getId(),
                        childId,
                        therapistId
                )
        );
    }
}
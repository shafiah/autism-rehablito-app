
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminAssignedTherapistResponse;
import com.autism_rehablito_app.dto.admin.AdminCommunicationMessageResponse;
import com.autism_rehablito_app.dto.admin.AdminCommunicationSummaryResponse;
import com.autism_rehablito_app.dto.admin.AdminMessageRequest;
import com.autism_rehablito_app.service.AdminCommunicationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/communication")
public class AdminCommunicationController {

    private final AdminCommunicationService adminCommunicationService;

    public AdminCommunicationController(
            AdminCommunicationService adminCommunicationService) {

        this.adminCommunicationService = adminCommunicationService;
    }

    /*
     * Get communication summary for all active children.
     */
    @GetMapping
    public ResponseEntity<List<AdminCommunicationSummaryResponse>>
    getCommunicationSummaries() {

        return ResponseEntity.ok(
                adminCommunicationService.getCommunicationSummaries()
        );
    }

    @GetMapping("/children/{childId}/messages")
    public ResponseEntity<List<AdminCommunicationMessageResponse>>
    getChildConversation(
            @PathVariable Long childId) {

        return ResponseEntity.ok(
                adminCommunicationService.getChildConversation(childId)
        );
    }
    
    @GetMapping("/children/{childId}/therapists/{therapistUserId}/messages")
    public ResponseEntity<List<AdminCommunicationMessageResponse>>
    getTherapistConversation(
            @PathVariable Long childId,
            @PathVariable Long therapistUserId) {

        return ResponseEntity.ok(
                adminCommunicationService.getTherapistConversation(
                        childId,
                        therapistUserId
                )
        );
    }

    @GetMapping("/children/{childId}/messages/{messageId}")
    public ResponseEntity<AdminCommunicationMessageResponse>
    getMessageById(
            @PathVariable Long childId,
            @PathVariable Long messageId) {

        return ResponseEntity.ok(
                adminCommunicationService.getMessageById(
                        childId,
                        messageId
                )
        );
    }

    @PostMapping("/children/{childId}/messages")
    public ResponseEntity<AdminCommunicationMessageResponse>
    sendMessage(
            @PathVariable Long childId,
            @Valid @RequestBody AdminMessageRequest request,
            Authentication authentication) {

        Long adminUserId =
                ((com.autism_rehablito_app.entity.User)
                        authentication.getPrincipal())
                        .getId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        adminCommunicationService.sendMessage(
                                adminUserId,
                                childId,
                                request
                        )
                );
    }
    
    @GetMapping("/children/{childId}/therapists")
    public ResponseEntity<List<AdminAssignedTherapistResponse>> getAssignedTherapists(
            @PathVariable Long childId) {

        return ResponseEntity.ok(
                adminCommunicationService.getAssignedTherapists(childId)
        );
    }
}

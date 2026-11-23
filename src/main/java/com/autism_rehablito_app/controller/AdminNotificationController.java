package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.NotificationMarkReadResponse;
import com.autism_rehablito_app.dto.NotificationResponse;
import com.autism_rehablito_app.dto.NotificationUnreadCountResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.autism_rehablito_app.dto.admin.AdminCreateNotificationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import com.autism_rehablito_app.dto.admin.AdminNotificationCampaignResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.AccessDeniedException;
import com.autism_rehablito_app.dto.admin.AdminNotificationRecipientResponse;
import com.autism_rehablito_app.dto.admin.AdminScheduleNotificationRequest;
import jakarta.validation.Valid;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notifications")
public class AdminNotificationController {

    private final NotificationService notificationService;

    public AdminNotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }
    

@PostMapping
public ResponseEntity<List<NotificationResponse>> createNotification(
        @Valid @RequestBody AdminCreateNotificationRequest request,
        Authentication authentication
) {

    User admin = getAuthenticatedUser(authentication);

    if (admin == null || admin.getRole() == null
            || !admin.getRole().name().equals("ADMIN")) {

        throw new org.springframework.security.access.AccessDeniedException(
                "Only Admin can create notifications"
        );
    }

    List<NotificationResponse> responses =
    		notificationService.createAdminNotifications(
    		        admin.getId(),
    		        request.getTitle(),
    		        request.getMessage(),
    		        request.getType(),
    		        request.getRecipient(),
    		        request.getBranchId()
    		);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(responses);
}


    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            Authentication authentication
    ) {
        User user = getAuthenticatedUser(authentication);

        return ResponseEntity.ok(
                notificationService.getNotifications(user.getId())
        );
    }
    

    /*
     * Admin notification campaign history
     */
    @GetMapping("/campaigns")
    public ResponseEntity<List<AdminNotificationCampaignResponse>> getCampaignHistory(
            Authentication authentication,

            @RequestParam(required = false) Long branchId,

            @RequestParam(required = false) String recipient,

            @RequestParam(required = false, defaultValue = "ALL") String status
    ) {

        User admin = getAuthenticatedUser(authentication);

        if (admin == null
                || admin.getRole() == null
                || !admin.getRole().name().equals("ADMIN")) {

            throw new AccessDeniedException("Only admins can access campaign history.");
        }

        List<AdminNotificationCampaignResponse> campaigns =
                notificationService.getAdminCampaignHistoryWithFilters(
                        admin.getId(),
                        branchId,
                        recipient,
                        status
                );

        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<NotificationUnreadCountResponse> getUnreadCount(
            Authentication authentication
    ) {
        User user = getAuthenticatedUser(authentication);

        return ResponseEntity.ok(
                notificationService.getUnreadCount(user.getId())
        );
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotification(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {
        User user = getAuthenticatedUser(authentication);

        return ResponseEntity.ok(
                notificationService.getNotification(
                        user.getId(),
                        notificationId
                )
        );
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationMarkReadResponse> markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {
        User user = getAuthenticatedUser(authentication);

        return ResponseEntity.ok(
                notificationService.markAsRead(
                        user.getId(),
                        notificationId
                )
        );
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            Authentication authentication
    ) {
        User user = getAuthenticatedUser(authentication);

        notificationService.markAllAsRead(user.getId());

        return ResponseEntity.noContent().build();
    }

    private User getAuthenticatedUser(
            Authentication authentication
    ) {
        return (User) authentication.getPrincipal();
    }
    

@GetMapping("/campaigns/{campaignId}/recipients")
public ResponseEntity<List<AdminNotificationRecipientResponse>> getCampaignRecipients(
        @PathVariable Long campaignId,
        Authentication authentication) {

    User admin = getAuthenticatedUser(authentication);

    if (admin == null
            || admin.getRole() == null
            || !admin.getRole().name().equals("ADMIN")) {

        throw new AccessDeniedException(
                "Only admins can access campaign recipients."
        );
    }

    List<AdminNotificationRecipientResponse> recipients =
            notificationService.getCampaignRecipients(
                    admin.getId(),
                    campaignId
            );

    return ResponseEntity.ok(recipients);
}


@PostMapping("/schedule")
public ResponseEntity<Map<String, Object>> scheduleNotification(
        @Valid @RequestBody AdminScheduleNotificationRequest request,
        Authentication authentication) {

    User admin = getAuthenticatedUser(authentication);

    if (admin == null
            || admin.getRole() == null
            || !admin.getRole().name().equals("ADMIN")) {

        throw new AccessDeniedException(
                "Only admins can schedule notifications."
        );
    }

    Long campaignId = notificationService.scheduleAdminNotification(
            admin.getId(),
            request
    );

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("message", "Notification scheduled successfully.");
    response.put("campaignId", campaignId);
    response.put("status", "SCHEDULED");
    response.put("scheduledAt", request.getScheduledAt());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}


@PutMapping("/schedule/{campaignId}")
public ResponseEntity<Map<String, Object>> updateScheduledNotification(
        @PathVariable Long campaignId,
        @Valid @RequestBody AdminScheduleNotificationRequest request,
        Authentication authentication) {

    User admin = getAuthenticatedUser(authentication);

    if (admin == null
            || admin.getRole() == null
            || !admin.getRole().name().equals("ADMIN")) {

        throw new AccessDeniedException(
                "Only admins can update scheduled notifications."
        );
    }

    Long updatedCampaignId =
            notificationService.updateScheduledNotification(
                    admin.getId(),
                    campaignId,
                    request
            );

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("message", "Scheduled notification updated successfully.");
    response.put("campaignId", updatedCampaignId);
    response.put("status", "SCHEDULED");
    response.put("scheduledAt", request.getScheduledAt());

    return ResponseEntity.ok(response);
}


@DeleteMapping("/schedule/{campaignId}")
public ResponseEntity<Map<String, Object>> cancelScheduledNotification(
        @PathVariable Long campaignId,
        Authentication authentication) {

    User admin = getAuthenticatedUser(authentication);

    if (admin == null
            || admin.getRole() == null
            || !admin.getRole().name().equals("ADMIN")) {

        throw new AccessDeniedException(
                "Only admins can cancel scheduled notifications."
        );
    }

    notificationService.cancelScheduledNotification(
            admin.getId(),
            campaignId
    );

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("message", "Scheduled notification cancelled successfully.");
    response.put("campaignId", campaignId);
    response.put("status", "CANCELLED");

    return ResponseEntity.ok(response);
}




}
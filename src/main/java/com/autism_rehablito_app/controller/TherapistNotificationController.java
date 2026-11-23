package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.NotificationMarkReadResponse;
import com.autism_rehablito_app.dto.NotificationResponse;
import com.autism_rehablito_app.dto.NotificationUnreadCountResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapist/notifications")
public class TherapistNotificationController {

    private final NotificationService notificationService;

    public TherapistNotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    /*
     * Get all notifications of logged-in therapist.
     */
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
     * Get unread notification count.
     */
    @GetMapping("/unread-count")
    public ResponseEntity<NotificationUnreadCountResponse> getUnreadCount(
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        return ResponseEntity.ok(
                notificationService.getUnreadCount(user.getId())
        );
    }

    /*
     * Get single notification.
     */
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

    /*
     * Mark single notification as read.
     */
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

    /*
     * Mark all notifications as read.
     */
    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        notificationService.markAllAsRead(user.getId());

        return ResponseEntity.noContent().build();
    }

    /*
     * Authentication principal is the actual User entity
     * configured in JwtAuthenticationFilter.
     */
    private User getAuthenticatedUser(
            Authentication authentication
    ) {

        return (User) authentication.getPrincipal();
    }
}
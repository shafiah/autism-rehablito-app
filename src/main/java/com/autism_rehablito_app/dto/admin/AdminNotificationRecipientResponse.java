
package com.autism_rehablito_app.dto.admin;

import java.time.LocalDateTime;

public class AdminNotificationRecipientResponse {

    private Long notificationId;
    private Long userId;
    private String recipientName;
    private String role;
    private Long branchId;
    private String branchName;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    public AdminNotificationRecipientResponse(
            Long notificationId,
            Long userId,
            String recipientName,
            String role,
            Long branchId,
            String branchName,
            Boolean isRead,
            LocalDateTime createdAt,
            LocalDateTime readAt) {

        this.notificationId = notificationId;
        this.userId = userId;
        this.recipientName = recipientName;
        this.role = role;
        this.branchId = branchId;
        this.branchName = branchName;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.readAt = readAt;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getRole() {
        return role;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }
}

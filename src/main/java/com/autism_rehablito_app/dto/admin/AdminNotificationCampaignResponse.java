
package com.autism_rehablito_app.dto.admin;

import com.autism_rehablito_app.entity.NotificationType;

import java.time.LocalDateTime;

public class AdminNotificationCampaignResponse {

    private Long campaignId;
    private String title;
    private String message;
    private NotificationType type;
    private String recipient;
    private Long branchId;
    private Integer totalRecipients;
    private Long readCount;
    private Long unreadCount;
    private LocalDateTime createdAt;
    private String status;
    private LocalDateTime scheduledAt;

    public AdminNotificationCampaignResponse(
            Long campaignId,
            String title,
            String message,
            NotificationType type,
            String recipient,
            Long branchId,
            Integer totalRecipients,
            Long readCount,
            Long unreadCount,
            LocalDateTime createdAt,
            String status,
            LocalDateTime scheduledAt
    ) {
        this.campaignId = campaignId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.recipient = recipient;
        this.branchId = branchId;
        this.totalRecipients = totalRecipients;
        this.readCount = readCount;
        this.unreadCount = unreadCount;
        this.createdAt = createdAt;
        this.status = status;
        this.scheduledAt = scheduledAt;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public String getRecipient() {
        return recipient;
    }

    public Long getBranchId() {
        return branchId;
    }

    public Integer getTotalRecipients() {
        return totalRecipients;
    }

    public Long getReadCount() {
        return readCount;
    }

    public Long getUnreadCount() {
        return unreadCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public String getStatus() {
        return status;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }
}

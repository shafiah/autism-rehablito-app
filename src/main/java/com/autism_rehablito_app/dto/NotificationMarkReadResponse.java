package com.autism_rehablito_app.dto;

import java.time.LocalDateTime;

public class NotificationMarkReadResponse {

    private Long notificationId;

    private Boolean isRead;

    private LocalDateTime readAt;

    public NotificationMarkReadResponse() {
    }

    public NotificationMarkReadResponse(
            Long notificationId,
            Boolean isRead,
            LocalDateTime readAt
    ) {
        this.notificationId = notificationId;
        this.isRead = isRead;
        this.readAt = readAt;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
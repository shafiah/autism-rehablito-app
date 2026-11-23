package com.autism_rehablito_app.dto;

import com.autism_rehablito_app.entity.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long notificationId;

    private Long branchId;

    private String title;

    private String message;

    private NotificationType type;

    private String referenceType;

    private Long referenceId;

    private Boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    public NotificationResponse() {
    }

    public NotificationResponse(
            Long notificationId,
            Long branchId,
            String title,
            String message,
            NotificationType type,
            String referenceType,
            Long referenceId,
            Boolean isRead,
            LocalDateTime createdAt,
            LocalDateTime readAt
    ) {
        this.notificationId = notificationId;
        this.branchId = branchId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.readAt = readAt;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
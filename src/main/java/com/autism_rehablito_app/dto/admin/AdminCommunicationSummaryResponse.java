
package com.autism_rehablito_app.dto.admin;

import java.time.LocalDateTime;

public class AdminCommunicationSummaryResponse {

    private Long childId;
    private String childName;

    private Long parentId;
    private Long parentUserId;
    private String parentName;

    private Long branchId;
    private String branchName;

    private Long therapistId;
    private Long therapistUserId;
    private String therapistName;

    private Long latestMessageId;
    private String latestMessage;
    private Long latestSenderUserId;
    private String latestSenderName;
    private String latestSenderRole;
    private Long latestReceiverUserId;
    private String latestReceiverName;
    private String latestReceiverRole;
    private LocalDateTime latestMessageAt;

    private Long parentUnreadCount;
    private Long therapistUnreadCount;

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Long getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(Long therapistId) {
        this.therapistId = therapistId;
    }

    public Long getTherapistUserId() {
        return therapistUserId;
    }

    public void setTherapistUserId(Long therapistUserId) {
        this.therapistUserId = therapistUserId;
    }

    public String getTherapistName() {
        return therapistName;
    }

    public void setTherapistName(String therapistName) {
        this.therapistName = therapistName;
    }

    public Long getLatestMessageId() {
        return latestMessageId;
    }

    public void setLatestMessageId(Long latestMessageId) {
        this.latestMessageId = latestMessageId;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public Long getLatestSenderUserId() {
        return latestSenderUserId;
    }

    public void setLatestSenderUserId(Long latestSenderUserId) {
        this.latestSenderUserId = latestSenderUserId;
    }

    public String getLatestSenderName() {
        return latestSenderName;
    }

    public void setLatestSenderName(String latestSenderName) {
        this.latestSenderName = latestSenderName;
    }

    public String getLatestSenderRole() {
        return latestSenderRole;
    }

    public void setLatestSenderRole(String latestSenderRole) {
        this.latestSenderRole = latestSenderRole;
    }

    public Long getLatestReceiverUserId() {
        return latestReceiverUserId;
    }

    public void setLatestReceiverUserId(Long latestReceiverUserId) {
        this.latestReceiverUserId = latestReceiverUserId;
    }

    public String getLatestReceiverName() {
        return latestReceiverName;
    }

    public void setLatestReceiverName(String latestReceiverName) {
        this.latestReceiverName = latestReceiverName;
    }

    public String getLatestReceiverRole() {
        return latestReceiverRole;
    }

    public void setLatestReceiverRole(String latestReceiverRole) {
        this.latestReceiverRole = latestReceiverRole;
    }

    public LocalDateTime getLatestMessageAt() {
        return latestMessageAt;
    }

    public void setLatestMessageAt(LocalDateTime latestMessageAt) {
        this.latestMessageAt = latestMessageAt;
    }

    public Long getParentUnreadCount() {
        return parentUnreadCount;
    }

    public void setParentUnreadCount(Long parentUnreadCount) {
        this.parentUnreadCount = parentUnreadCount;
    }

    public Long getTherapistUnreadCount() {
        return therapistUnreadCount;
    }

    public void setTherapistUnreadCount(Long therapistUnreadCount) {
        this.therapistUnreadCount = therapistUnreadCount;
    }
}

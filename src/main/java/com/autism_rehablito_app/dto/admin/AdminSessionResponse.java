package com.autism_rehablito_app.dto.admin;

import com.autism_rehablito_app.entity.SessionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AdminSessionResponse {

    private Long sessionId;

    private Long childId;
    private String childName;

    private Long therapistId;
    private String therapistName;
    private String therapyType;

    private Long branchId;
    private String branchName;

    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private SessionStatus status;

    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AdminSessionResponse(
            Long sessionId,
            Long childId,
            String childName,
            Long therapistId,
            String therapistName,
            String therapyType,
            Long branchId,
            String branchName,
            LocalDate sessionDate,
            LocalTime startTime,
            LocalTime endTime,
            SessionStatus status,
            String remarks,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.sessionId = sessionId;
        this.childId = childId;
        this.childName = childName;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.therapyType = therapyType;
        this.branchId = branchId;
        this.branchName = branchName;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getChildId() {
        return childId;
    }

    public String getChildName() {
        return childName;
    }

    public Long getTherapistId() {
        return therapistId;
    }

    public String getTherapistName() {
        return therapistName;
    }

    public String getTherapyType() {
        return therapyType;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
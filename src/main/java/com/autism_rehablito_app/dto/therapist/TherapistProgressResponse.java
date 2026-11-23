package com.autism_rehablito_app.dto.therapist;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TherapistProgressResponse {

    private Long progressId;

    private Long childId;
    private String childName;

    private Long therapistId;
    private String therapistName;

    private Long sessionId;

    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String observations;
    private String progress;
    private String goals;
    private String activities;
    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TherapistProgressResponse(
            Long progressId,
            Long childId,
            String childName,
            Long therapistId,
            String therapistName,
            Long sessionId,
            LocalDate sessionDate,
            LocalTime startTime,
            LocalTime endTime,
            String observations,
            String progress,
            String goals,
            String activities,
            String remarks,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.progressId = progressId;
        this.childId = childId;
        this.childName = childName;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.sessionId = sessionId;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.observations = observations;
        this.progress = progress;
        this.goals = goals;
        this.activities = activities;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getProgressId() {
        return progressId;
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

    public Long getSessionId() {
        return sessionId;
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

    public String getObservations() {
        return observations;
    }

    public String getProgress() {
        return progress;
    }

    public String getGoals() {
        return goals;
    }

    public String getActivities() {
        return activities;
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
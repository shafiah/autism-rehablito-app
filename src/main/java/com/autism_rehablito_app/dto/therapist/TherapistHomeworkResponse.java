package com.autism_rehablito_app.dto.therapist;

import com.autism_rehablito_app.entity.HomeworkStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TherapistHomeworkResponse {

    private Long homeworkId;

    private Long childId;
    private String childName;

    private Long therapistId;
    private String therapistName;

    private Long sessionId;

    private String title;
    private String instructions;

    private LocalDate assignedDate;
    private LocalDate dueDate;

    private HomeworkStatus status;

    private String parentRemarks;
    
    private String therapistRemarks;
    
    private String parentName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TherapistHomeworkResponse(
            Long homeworkId,
            Long childId,
            String childName,
            Long therapistId,
            String therapistName,
            Long sessionId,
            String title,
            String instructions,
            LocalDate assignedDate,
            LocalDate dueDate,
            HomeworkStatus status,
            String parentRemarks,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.homeworkId = homeworkId;
        this.childId = childId;
        this.childName = childName;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.sessionId = sessionId;
        this.title = title;
        this.instructions = instructions;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
        this.status = status;
        this.parentRemarks = parentRemarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getHomeworkId() {
        return homeworkId;
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

    public String getTitle() {
        return title;
    }

    public String getInstructions() {
        return instructions;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public HomeworkStatus getStatus() {
        return status;
    }

    public String getParentRemarks() {
        return parentRemarks;
    }
    
    public String getTherapistRemarks() {
        return therapistRemarks;
    }
    
    public void setTherapistRemarks(String therapistRemarks) {
        this.therapistRemarks = therapistRemarks;
    }
    
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
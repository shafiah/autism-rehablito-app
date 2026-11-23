
package com.autism_rehablito_app.dto.parent;

import com.autism_rehablito_app.entity.HomeworkStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ParentHomeworkResponse {

    private Long homeworkId;

    private Long childId;
    private String childName;

    private Long therapistId;
    private String therapistName;

    private Long sessionId;

    private String therapyType;

    private String title;
    private String instructions;

    private LocalDate assignedDate;
    private LocalDate dueDate;

    private HomeworkStatus status;

    private String parentRemarks;
    private String therapistRemarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ParentHomeworkResponse(
            Long homeworkId,
            Long childId,
            String childName,
            Long therapistId,
            String therapistName,
            Long sessionId,
            String therapyType,
            String title,
            String instructions,
            LocalDate assignedDate,
            LocalDate dueDate,
            HomeworkStatus status,
            String parentRemarks,
            String therapistRemarks,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.homeworkId = homeworkId;
        this.childId = childId;
        this.childName = childName;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.sessionId = sessionId;
        this.therapyType = therapyType;
        this.title = title;
        this.instructions = instructions;
        this.assignedDate = assignedDate;
        this.dueDate = dueDate;
        this.status = status;
        this.parentRemarks = parentRemarks;
        this.therapistRemarks = therapistRemarks;
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

    public String getTherapyType() {
        return therapyType;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

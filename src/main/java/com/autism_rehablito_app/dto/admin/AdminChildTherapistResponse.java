package com.autism_rehablito_app.dto.admin;

import java.time.LocalDateTime;

public class AdminChildTherapistResponse {

    private Long assignmentId;

    private Long childId;
    private String childName;

    private Long therapistId;
    private String therapistName;
    private String therapistEmail;
    private String therapyType;

    private Long branchId;
    private String branchName;

    private Boolean active;
    private LocalDateTime assignedAt;
    private LocalDateTime unassignedAt;

    public AdminChildTherapistResponse(
            Long assignmentId,
            Long childId,
            String childName,
            Long therapistId,
            String therapistName,
            String therapistEmail,
            String therapyType,
            Long branchId,
            String branchName,
            Boolean active,
            LocalDateTime assignedAt,
            LocalDateTime unassignedAt) {

        this.assignmentId = assignmentId;
        this.childId = childId;
        this.childName = childName;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.therapistEmail = therapistEmail;
        this.therapyType = therapyType;
        this.branchId = branchId;
        this.branchName = branchName;
        this.active = active;
        this.assignedAt = assignedAt;
        this.unassignedAt = unassignedAt;
    }

    public Long getAssignmentId() {
        return assignmentId;
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

    public String getTherapistEmail() {
        return therapistEmail;
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

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public LocalDateTime getUnassignedAt() {
        return unassignedAt;
    }
}
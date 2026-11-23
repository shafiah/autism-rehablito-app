package com.autism_rehablito_app.dto.parent;

import java.time.LocalDateTime;

public class ParentChildTherapistResponse {

    private Long assignmentId;

    private Long therapistId;
    private String therapistName;
    private String therapyType;

    private Long branchId;
    private String branchName;

    private Boolean active;
    private LocalDateTime assignedAt;

    public ParentChildTherapistResponse(
            Long assignmentId,
            Long therapistId,
            String therapistName,
            String therapyType,
            Long branchId,
            String branchName,
            Boolean active,
            LocalDateTime assignedAt) {

        this.assignmentId = assignmentId;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.therapyType = therapyType;
        this.branchId = branchId;
        this.branchName = branchName;
        this.active = active;
        this.assignedAt = assignedAt;
    }

    public Long getAssignmentId() {
        return assignmentId;
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

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
}
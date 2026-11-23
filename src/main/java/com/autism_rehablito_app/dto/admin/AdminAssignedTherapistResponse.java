package com.autism_rehablito_app.dto.admin;

public class AdminAssignedTherapistResponse {

    private Long therapistId;
    private Long therapistUserId;
    private String therapistName;
    private String therapyType;

    public AdminAssignedTherapistResponse() {
    }

    public AdminAssignedTherapistResponse(
            Long therapistId,
            Long therapistUserId,
            String therapistName,
            String therapyType) {
        this.therapistId = therapistId;
        this.therapistUserId = therapistUserId;
        this.therapistName = therapistName;
        this.therapyType = therapyType;
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

    public String getTherapyType() {
        return therapyType;
    }

    public void setTherapyType(String therapyType) {
        this.therapyType = therapyType;
    }
}

package com.autism_rehablito_app.dto.admin;

public class AdminTherapistCreateResponse {

    private Long userId;
    private Long therapistId;
    private String name;
    private String email;
    private String mobileNumber;
    private String role;
    private Long branchId;
    private String branchName;
    private String therapyType;
    private String availability;
    private Boolean active;
    private String message;

    public AdminTherapistCreateResponse(
            Long userId,
            Long therapistId,
            String name,
            String email,
            String mobileNumber,
            String role,
            Long branchId,
            String branchName,
            String therapyType,
            String availability,
            Boolean active,
            String message) {

        this.userId = userId;
        this.therapistId = therapistId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.role = role;
        this.branchId = branchId;
        this.branchName = branchName;
        this.therapyType = therapyType;
        this.availability = availability;
        this.active = active;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTherapistId() {
        return therapistId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getRole() {
        return role;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getTherapyType() {
        return therapyType;
    }

    public String getAvailability() {
        return availability;
    }

    public Boolean getActive() {
        return active;
    }

    public String getMessage() {
        return message;
    }
}
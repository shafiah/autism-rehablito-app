package com.autism_rehablito_app.dto.therapist;

public class TherapistLoginResponse {

    private Long userId;
    private Long therapistId;
    private String name;
    private String email;
    private String role;
    private Long branchId;
    private String branchName;
    private String therapyType;
    private String token;

    public TherapistLoginResponse(
            Long userId,
            Long therapistId,
            String name,
            String email,
            String role,
            Long branchId,
            String branchName,
            String therapyType,
            String token) {

        this.userId = userId;
        this.therapistId = therapistId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.branchId = branchId;
        this.branchName = branchName;
        this.therapyType = therapyType;
        this.token = token;
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

    public String getToken() {
        return token;
    }
}
package com.autism_rehablito_app.dto.parent;

public class ParentRegisterResponse {

    private Long userId;
    private Long parentId;
    private String name;
    private String email;
    private String mobileNumber;
    private String role;
    private String message;

    public ParentRegisterResponse(
            Long userId,
            Long parentId,
            String name,
            String email,
            String mobileNumber,
            String role,
            String message) {

        this.userId = userId;
        this.parentId = parentId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.role = role;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getParentId() {
        return parentId;
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

    public String getMessage() {
        return message;
    }
}
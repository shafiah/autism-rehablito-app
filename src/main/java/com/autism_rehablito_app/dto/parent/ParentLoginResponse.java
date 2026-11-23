package com.autism_rehablito_app.dto.parent;

public class ParentLoginResponse {

    private Long userId;
    private Long parentId;
    private String name;
    private String email;
    private String role;
    private String token;

    public ParentLoginResponse(
            Long userId,
            Long parentId,
            String name,
            String email,
            String role,
            String token) {

        this.userId = userId;
        this.parentId = parentId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.token = token;
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

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
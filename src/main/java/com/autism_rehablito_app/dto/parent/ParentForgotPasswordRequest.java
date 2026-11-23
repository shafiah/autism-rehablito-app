package com.autism_rehablito_app.dto.parent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ParentForgotPasswordRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
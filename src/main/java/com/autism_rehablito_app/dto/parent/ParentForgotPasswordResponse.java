package com.autism_rehablito_app.dto.parent;

public class ParentForgotPasswordResponse {

    private String message;

    public ParentForgotPasswordResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
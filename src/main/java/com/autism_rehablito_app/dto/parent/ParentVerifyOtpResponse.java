package com.autism_rehablito_app.dto.parent;

public class ParentVerifyOtpResponse {

    private String message;
    private String resetToken;

    public ParentVerifyOtpResponse(
            String message,
            String resetToken) {

        this.message = message;
        this.resetToken = resetToken;
    }

    public String getMessage() {
        return message;
    }

    public String getResetToken() {
        return resetToken;
    }
}
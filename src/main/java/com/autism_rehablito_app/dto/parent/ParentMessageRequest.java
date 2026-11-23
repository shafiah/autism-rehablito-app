package com.autism_rehablito_app.dto.parent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ParentMessageRequest {

    @NotBlank(message = "Message is required")
    @Size(max = 3000, message = "Message cannot exceed 3000 characters")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
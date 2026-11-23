package com.autism_rehablito_app.dto.therapist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TherapistMessageRequest {

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
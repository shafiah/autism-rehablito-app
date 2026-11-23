package com.autism_rehablito_app.dto.therapist;

import com.autism_rehablito_app.entity.HomeworkStatus;

import jakarta.validation.constraints.NotNull;

public class TherapistHomeworkStatusUpdateRequest {

    @NotNull(message = "Homework status is required")
    private HomeworkStatus status;

    public HomeworkStatus getStatus() {
        return status;
    }

    public void setStatus(HomeworkStatus status) {
        this.status = status;
    }
}
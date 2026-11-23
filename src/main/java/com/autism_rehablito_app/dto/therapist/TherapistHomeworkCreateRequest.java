
package com.autism_rehablito_app.dto.therapist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TherapistHomeworkCreateRequest {

    @NotBlank(message = "Homework title is required")
    @Size(max = 200, message = "Homework title cannot exceed 200 characters")
    private String title;

    @NotBlank(message = "Homework instructions are required")
    @Size(max = 3000, message = "Homework instructions cannot exceed 3000 characters")
    private String instructions;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    private Long sessionId;

    @NotBlank(message = "Therapy type is required")
    private String therapyType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getTherapyType() {
        return therapyType;
    }

    public void setTherapyType(String therapyType) {
        this.therapyType = therapyType;
    }
}

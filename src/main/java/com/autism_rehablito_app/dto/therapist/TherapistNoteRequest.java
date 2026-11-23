
package com.autism_rehablito_app.dto.therapist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TherapistNoteRequest {

    @NotBlank(message = "Therapist note cannot be empty")
    @Size(max = 2000, message = "Therapist note cannot exceed 2000 characters")
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

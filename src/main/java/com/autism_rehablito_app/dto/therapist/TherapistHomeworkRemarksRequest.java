
package com.autism_rehablito_app.dto.therapist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TherapistHomeworkRemarksRequest {

    @NotBlank(message = "Therapist remarks are required")
    @Size(
            max = 2000,
            message = "Therapist remarks cannot exceed 2000 characters"
    )
    private String therapistRemarks;

    public TherapistHomeworkRemarksRequest() {
    }

    public String getTherapistRemarks() {
        return therapistRemarks;
    }

    public void setTherapistRemarks(String therapistRemarks) {
        this.therapistRemarks = therapistRemarks;
    }
}

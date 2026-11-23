package com.autism_rehablito_app.dto.therapist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TherapistReportCreateRequest {

    @NotBlank(message = "Report title is required")
    @Size(max = 200, message = "Report title cannot exceed 200 characters")
    private String reportTitle;

    @NotBlank(message = "Report content is required")
    @Size(max = 5000, message = "Report content cannot exceed 5000 characters")
    private String reportContent;

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }
}
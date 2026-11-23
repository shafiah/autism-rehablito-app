package com.autism_rehablito_app.dto.admin;

import jakarta.validation.constraints.Size;

public class AdminReportReviewRequest {

    @Size(max = 2000, message = "Admin remarks cannot exceed 2000 characters")
    private String adminRemarks;

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }
}
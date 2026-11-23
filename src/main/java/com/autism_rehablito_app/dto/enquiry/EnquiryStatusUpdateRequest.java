package com.autism_rehablito_app.dto.enquiry;

import jakarta.validation.constraints.NotBlank;

public class EnquiryStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status;

    public EnquiryStatusUpdateRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
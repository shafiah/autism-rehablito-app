package com.autism_rehablito_app.dto.enquiry;

import java.time.LocalDateTime;

public class EnquiryResponse {

    private Long id;
    private String parentName;
    private String mobileNumber;
    private String childAge;
    private String enquiryPurpose;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EnquiryResponse(
            Long id,
            String parentName,
            String mobileNumber,
            String childAge,
            String enquiryPurpose,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.parentName = parentName;
        this.mobileNumber = mobileNumber;
        this.childAge = childAge;
        this.enquiryPurpose = enquiryPurpose;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getParentName() {
        return parentName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getChildAge() {
        return childAge;
    }

    public String getEnquiryPurpose() {
        return enquiryPurpose;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
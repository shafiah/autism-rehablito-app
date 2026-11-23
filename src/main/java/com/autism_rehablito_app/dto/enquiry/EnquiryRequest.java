
package com.autism_rehablito_app.dto.enquiry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EnquiryRequest {

    @NotBlank(message = "Parent name is required")
    @Size(max = 100, message = "Parent name cannot exceed 100 characters")
    private String parentName;

    @NotBlank(message = "Mobile number is required")
    @Size(max = 15, message = "Mobile number cannot exceed 15 characters")
    private String mobileNumber;

    @Size(max = 50, message = "Child age cannot exceed 50 characters")
    private String childAge;

    @Size(max = 500, message = "Enquiry purpose cannot exceed 500 characters")
    private String enquiryPurpose;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public String getEnquiryPurpose() {
        return enquiryPurpose;
    }

    public void setEnquiryPurpose(String enquiryPurpose) {
        this.enquiryPurpose = enquiryPurpose;
    }
}

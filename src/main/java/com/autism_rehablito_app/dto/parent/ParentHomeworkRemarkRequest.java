package com.autism_rehablito_app.dto.parent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ParentHomeworkRemarkRequest {

    @NotBlank(message = "Parent remark is required")
    @Size(
            max = 2000,
            message = "Parent remark cannot exceed 2000 characters"
    )
    private String parentRemarks;

    public String getParentRemarks() {
        return parentRemarks;
    }

    public void setParentRemarks(String parentRemarks) {
        this.parentRemarks = parentRemarks;
    }
}
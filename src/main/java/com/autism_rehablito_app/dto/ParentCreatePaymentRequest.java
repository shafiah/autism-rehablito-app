package com.autism_rehablito_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ParentCreatePaymentRequest {

    @NotNull(message = "Child ID is required")
    private Long childId;

    @NotBlank(message = "Therapy type is required")
    private String therapyType;

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getTherapyType() {
        return therapyType;
    }

    public void setTherapyType(String therapyType) {
        this.therapyType = therapyType;
    }
}
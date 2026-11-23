package com.autism_rehablito_app.dto.parent;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ParentChildResponse {

    private Long childId;
    private String childName;
    private LocalDate dateOfBirth;
    private String gender;

    private Long branchId;
    private String branchName;

    private String medicalNotes;
    private String imagePath;
    private Boolean active;
    private LocalDateTime createdAt;

    public ParentChildResponse(
            Long childId,
            String childName,
            LocalDate dateOfBirth,
            String gender,
            Long branchId,
            String branchName,
            String medicalNotes,
            Boolean active,
            LocalDateTime createdAt,
            String imagePath
            ) {

        this.childId = childId;
        this.childName = childName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.branchId = branchId;
        this.branchName = branchName;
        this.medicalNotes = medicalNotes;
        this.active = active;
        this.createdAt = createdAt;
        this.imagePath = imagePath;
    }

    public Long getChildId() {
        return childId;
    }

    public String getChildName() {
        return childName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getMedicalNotes() {
        return medicalNotes;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
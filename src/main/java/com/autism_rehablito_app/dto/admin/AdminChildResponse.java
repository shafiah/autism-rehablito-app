package com.autism_rehablito_app.dto.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AdminChildResponse {

    private Long childId;
    private String childName;
    private LocalDate dateOfBirth;
    private String gender;

    private Long parentId;
    private String parentName;
    private String parentEmail;
    private String parentMobileNumber;

    private Long branchId;
    private String branchName;

    private String medicalNotes;
    private String imagePath;
    private Boolean active;
    private LocalDateTime createdAt;
    private List<String> therapyTypes;

    public AdminChildResponse(
            Long childId,
            String childName,
            LocalDate dateOfBirth,
            String gender,
            Long parentId,
            String parentName,
            String parentEmail,
            String parentMobileNumber,
            Long branchId,
            String branchName,
            String medicalNotes,
            Boolean active,
            LocalDateTime createdAt,
            String imagePath) {

        this.childId = childId;
        this.childName = childName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.parentId = parentId;
        this.parentName = parentName;
        this.parentEmail = parentEmail;
        this.parentMobileNumber = parentMobileNumber;
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

    public Long getParentId() {
        return parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public String getParentMobileNumber() {
        return parentMobileNumber;
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
    
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public List<String> getTherapyTypes() {
        return therapyTypes;
    }

    public void setTherapyTypes(List<String> therapyTypes) {
        this.therapyTypes = therapyTypes;
    }
}
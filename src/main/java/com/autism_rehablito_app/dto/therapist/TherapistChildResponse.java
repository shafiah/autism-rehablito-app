
package com.autism_rehablito_app.dto.therapist;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TherapistChildResponse {

    private Long childId;
    private String childName;
    private LocalDate dateOfBirth;
    private String gender;

    private String parentName;

    private Long branchId;
    private String branchName;

    private String medicalNotes;
    private Boolean active;
    private LocalDateTime createdAt;

    public TherapistChildResponse(
            Long childId,
            String childName,
            LocalDate dateOfBirth,
            String gender,
            String parentName,
            Long branchId,
            String branchName,
            String medicalNotes,
            Boolean active,
            LocalDateTime createdAt) {

        this.childId = childId;
        this.childName = childName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.parentName = parentName;
        this.branchId = branchId;
        this.branchName = branchName;
        this.medicalNotes = medicalNotes;
        this.active = active;
        this.createdAt = createdAt;
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

    public String getParentName() {
        return parentName;
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
}

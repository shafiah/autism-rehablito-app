package com.autism_rehablito_app.dto.therapist;

import jakarta.validation.constraints.Size;

public class TherapistProgressRequest {

    @Size(max = 2000, message = "Observations cannot exceed 2000 characters")
    private String observations;

    @Size(max = 2000, message = "Progress cannot exceed 2000 characters")
    private String progress;

    @Size(max = 2000, message = "Goals cannot exceed 2000 characters")
    private String goals;

    @Size(max = 2000, message = "Activities cannot exceed 2000 characters")
    private String activities;

    @Size(max = 2000, message = "Remarks cannot exceed 2000 characters")
    private String remarks;

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
package com.autism_rehablito_app.dto.admin;

import com.autism_rehablito_app.entity.ReportStatus;

import java.time.LocalDateTime;

public class AdminReportResponse {

    private Long reportId;

    private Long childId;
    private String childName;

    private Long therapistId;
    private String therapistName;

    private String reportTitle;
    private String reportContent;

    private ReportStatus status;

    private String adminRemarks;
    
    private Long branchId;
    private String branchName;
    private String therapyType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime publishedAt;

    public AdminReportResponse(
            Long reportId,
            Long childId,
            String childName,
            Long therapistId,
            String therapistName,
            String reportTitle,
            String reportContent,
            ReportStatus status,
            String adminRemarks,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime submittedAt,
            LocalDateTime reviewedAt,
            LocalDateTime publishedAt) {

        this.reportId = reportId;
        this.childId = childId;
        this.childName = childName;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.reportTitle = reportTitle;
        this.reportContent = reportContent;
        this.status = status;
        this.adminRemarks = adminRemarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.submittedAt = submittedAt;
        this.reviewedAt = reviewedAt;
        this.publishedAt = publishedAt;
    }

    public Long getReportId() {
        return reportId;
    }

    public Long getChildId() {
        return childId;
    }

    public String getChildName() {
        return childName;
    }

    public Long getTherapistId() {
        return therapistId;
    }

    public String getTherapistName() {
        return therapistName;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public String getReportContent() {
        return reportContent;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }
    
    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getTherapyType() {
        return therapyType;
    }

    public void setTherapyType(String therapyType) {
        this.therapyType = therapyType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
}
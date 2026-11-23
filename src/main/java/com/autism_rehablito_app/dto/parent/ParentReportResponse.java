package com.autism_rehablito_app.dto.parent;

import com.autism_rehablito_app.entity.ReportStatus;

import java.time.LocalDateTime;

public class ParentReportResponse {

    private Long reportId;

    private Long childId;
    private String childName;

    private Long therapistId;
    private String therapistName;

    private String reportTitle;
    private String reportContent;

    private ReportStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime publishedAt;

    public ParentReportResponse(
            Long reportId,
            Long childId,
            String childName,
            Long therapistId,
            String therapistName,
            String reportTitle,
            String reportContent,
            ReportStatus status,
            LocalDateTime createdAt,
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
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
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
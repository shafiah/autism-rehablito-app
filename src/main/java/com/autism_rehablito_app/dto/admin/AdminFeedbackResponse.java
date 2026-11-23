
package com.autism_rehablito_app.dto.admin;

import com.autism_rehablito_app.entity.Feedback;
import com.autism_rehablito_app.entity.FeedbackStatus;

import java.time.LocalDateTime;

public class AdminFeedbackResponse {

    private Long feedbackId;

    private Long parentId;
    private String parentName;

    private Long childId;
    private String childName;

    private Long therapistId;
    private String therapistName;

    private Integer rating;
    private String message;

    private FeedbackStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    public AdminFeedbackResponse(Feedback feedback) {

        this.feedbackId = feedback.getId();

        this.parentId = feedback.getParent().getId();
        this.parentName = feedback.getParent().getUser().getName();

        this.childId = feedback.getChild().getId();
        this.childName = feedback.getChild().getChildName();

        this.therapistId = feedback.getTherapist().getId();
        this.therapistName = feedback.getTherapist().getUser().getName();

        this.rating = feedback.getRating();
        this.message = feedback.getMessage();

        this.status = feedback.getStatus();

        this.createdAt = feedback.getCreatedAt();
        this.publishedAt = feedback.getPublishedAt();
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getParentName() {
        return parentName;
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

    public Integer getRating() {
        return rating;
    }

    public String getMessage() {
        return message;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
}

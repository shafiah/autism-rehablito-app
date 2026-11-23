
package com.autism_rehablito_app.dto.parent;

import com.autism_rehablito_app.entity.Feedback;
import com.autism_rehablito_app.entity.FeedbackStatus;

import java.time.LocalDateTime;

public class ParentFeedbackResponse {

    private Long feedbackId;
    private Long childId;
    private Long therapistId;
    private Integer rating;
    private String message;
    private FeedbackStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    public ParentFeedbackResponse(Feedback feedback) {
        this.feedbackId = feedback.getId();
        this.childId = feedback.getChild().getId();
        this.therapistId = feedback.getTherapist().getId();
        this.rating = feedback.getRating();
        this.message = feedback.getMessage();
        this.status = feedback.getStatus();
        this.createdAt = feedback.getCreatedAt();
        this.publishedAt = feedback.getPublishedAt();
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public Long getChildId() {
        return childId;
    }

    public Long getTherapistId() {
        return therapistId;
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

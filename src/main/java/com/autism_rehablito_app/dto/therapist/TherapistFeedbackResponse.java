
package com.autism_rehablito_app.dto.therapist;

import com.autism_rehablito_app.entity.Feedback;
import com.autism_rehablito_app.entity.FeedbackStatus;

import java.time.LocalDateTime;

public class TherapistFeedbackResponse {

    private Long feedbackId;

    private Long childId;
    private String childName;

    private String parentName;

    private Integer rating;
    private String message;

    private FeedbackStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    public TherapistFeedbackResponse(Feedback feedback) {

        this.feedbackId = feedback.getId();

        this.childId = feedback.getChild().getId();
        this.childName = feedback.getChild().getChildName();

        this.parentName = feedback.getParent()
                .getUser().getName();

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

    public String getChildName() {
        return childName;
    }

    public String getParentName() {
        return parentName;
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

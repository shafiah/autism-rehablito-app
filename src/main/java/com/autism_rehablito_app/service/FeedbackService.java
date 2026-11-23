
package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.parent.ParentFeedbackRequest;
import com.autism_rehablito_app.entity.Feedback;

import java.util.List;

public interface FeedbackService {

    // Parent submits feedback
    Feedback submitFeedback(
            Long userId,
            ParentFeedbackRequest request
    );

    // Parent views own feedback history
    List<Feedback> getParentFeedbackHistory(Long userId);

    // Admin views all feedback
    List<Feedback> getAllFeedback();

    // Admin publishes feedback
    Feedback publishFeedback(Long feedbackId);

    // Therapist views only published feedback
    List<Feedback> getPublishedFeedbackForTherapist(Long userId);
}

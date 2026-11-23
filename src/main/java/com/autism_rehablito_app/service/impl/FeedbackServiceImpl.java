
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.parent.ParentFeedbackRequest;
import com.autism_rehablito_app.entity.*;
import com.autism_rehablito_app.repository.*;
import com.autism_rehablito_app.service.FeedbackService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final TherapistRepository therapistRepository;
    private final ChildTherapistRepository childTherapistRepository;

    public FeedbackServiceImpl(
            FeedbackRepository feedbackRepository,
            ParentRepository parentRepository,
            ChildRepository childRepository,
            TherapistRepository therapistRepository,
            ChildTherapistRepository childTherapistRepository
    ) {
        this.feedbackRepository = feedbackRepository;
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.therapistRepository = therapistRepository;
        this.childTherapistRepository = childTherapistRepository;
    }

    // Parent submits feedback
    @Override
    public Feedback submitFeedback(
            Long userId,
            ParentFeedbackRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException("Feedback request is required");
        }

        if (request.getChildId() == null ||
                request.getTherapistId() == null) {
            throw new IllegalArgumentException(
                    "Child ID and Therapist ID are required"
            );
        }

        if (request.getRating() == null ||
                request.getRating() < 1 ||
                request.getRating() > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5"
            );
        }

        if (request.getMessage() == null ||
                request.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Feedback message is required"
            );
        }

        String message = request.getMessage().trim();

        if (message.length() > 500) {
            throw new IllegalArgumentException(
                    "Feedback message cannot exceed 500 characters"
            );
        }

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Parent not found")
                );

        Child child = childRepository.findById(request.getChildId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Child not found")
                );

        // Verify that child belongs to logged-in parent
        if (!Objects.equals(child.getParent().getId(), parent.getId())) {
            throw new IllegalArgumentException(
                    "You are not authorized to submit feedback for this child"
            );
        }

        Therapist therapist = therapistRepository
                .findById(request.getTherapistId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Therapist not found")
                );

        // Verify active child-therapist assignment
        ChildTherapist assignment = childTherapistRepository
                .findByChildIdAndTherapistIdAndActive(
                        child.getId(),
                        therapist.getId(),
                        true
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "This therapist is not currently assigned to this child"
                        )
                );

        if (!Boolean.TRUE.equals(assignment.getActive())) {
            throw new IllegalArgumentException(
                    "Therapist assignment is inactive"
            );
        }

        Feedback feedback = new Feedback();

        feedback.setParent(parent);
        feedback.setChild(child);
        feedback.setTherapist(therapist);
        feedback.setRating(request.getRating());
        feedback.setMessage(message);
        feedback.setStatus(FeedbackStatus.SUBMITTED);

        // Every submission creates a new feedback record
        Feedback savedFeedback = feedbackRepository.save(feedback);

        return feedbackRepository
                .findWithDetailsById(savedFeedback.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unable to retrieve saved feedback"
                        )
                );
    }
    

    // Parent views own feedback history
    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getParentFeedbackHistory(Long userId) {

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Parent not found")
                );

        return feedbackRepository
                .findByParentIdOrderByCreatedAtDesc(parent.getId());
    }

    // Admin views all feedback
    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getAllFeedback() {

        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }

    // Admin publishes feedback
    @Override
    public Feedback publishFeedback(Long feedbackId) {

        Feedback feedback = feedbackRepository
                .findWithDetailsById(feedbackId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Feedback not found")
                );

        if (feedback.getStatus() == FeedbackStatus.PUBLISHED) {
            throw new IllegalArgumentException(
                    "Feedback is already published"
            );
        }

        feedback.setStatus(FeedbackStatus.PUBLISHED);

        feedback.setPublishedAt(
                LocalDateTime.now(
                        java.time.ZoneId.of("Asia/Kolkata")
                )
        );

        return feedbackRepository.save(feedback);
    }
    // Therapist views only published feedback
    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getPublishedFeedbackForTherapist(Long userId) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Therapist not found")
                );

        return feedbackRepository
                .findByTherapistIdAndStatusOrderByCreatedAtDesc(
                        therapist.getId(),
                        FeedbackStatus.PUBLISHED
                );
    }
}

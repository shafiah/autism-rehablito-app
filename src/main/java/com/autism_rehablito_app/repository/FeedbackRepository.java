
package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.Feedback;
import com.autism_rehablito_app.entity.FeedbackStatus;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Parent: own feedback history
	@EntityGraph(attributePaths = {
	        "parent.user",
	        "child",
	        "therapist.user"
	})
	List<Feedback> findByParentIdOrderByCreatedAtDesc(Long parentId);

    // Admin: all feedback with Parent, Child and Therapist details
    @EntityGraph(attributePaths = {
            "parent.user",
            "child",
            "therapist.user"
    })
    List<Feedback> findAllByOrderByCreatedAtDesc();

    // Admin: feedback filtered by status
    @EntityGraph(attributePaths = {
            "parent.user",
            "child",
            "therapist.user"
    })
    List<Feedback> findByStatusOrderByCreatedAtDesc(
            FeedbackStatus status
    );

    // Therapist: only published feedback with related details
    @EntityGraph(attributePaths = {
            "parent.user",
            "child",
            "therapist.user"
    })
    List<Feedback> findByTherapistIdAndStatusOrderByCreatedAtDesc(
            Long therapistId,
            FeedbackStatus status
    );

    // Fetch complete details for individual feedback
    @EntityGraph(attributePaths = {
            "parent.user",
            "child",
            "therapist.user"
    })
    Optional<Feedback> findWithDetailsById(Long id);
}

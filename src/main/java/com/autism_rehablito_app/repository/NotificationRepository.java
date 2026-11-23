package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /*
     * Get all notifications of a specific user,
     * newest notification first.
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    /*
     * Get all notifications of a specific user
     * for a specific branch.
     */
    List<Notification> findByUserIdAndBranchIdOrderByCreatedAtDesc(
            Long userId,
            Long branchId
    );

    /*
     * Get unread notifications of a specific user.
     */
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(
            Long userId
    );

    /*
     * Get unread notifications of a specific user
     * within a specific branch.
     */
    List<Notification> findByUserIdAndBranchIdAndIsReadFalseOrderByCreatedAtDesc(
            Long userId,
            Long branchId
    );

    /*
     * Get a specific notification belonging to a specific user.
     * Used for ownership/security validation.
     */
    Optional<Notification> findByIdAndUserId(
            Long notificationId,
            Long userId
    );

    /*
     * Get a specific notification belonging to a specific user
     * and branch.
     */
    Optional<Notification> findByIdAndUserIdAndBranchId(
            Long notificationId,
            Long userId,
            Long branchId
    );

    /*
     * Count unread notifications of a specific user.
     */
    long countByUserIdAndIsReadFalse(Long userId);

    /*
     * Count unread notifications of a specific user
     * within a specific branch.
     */
    long countByUserIdAndBranchIdAndIsReadFalse(
            Long userId,
            Long branchId
    );

    /*
     * Used when marking all notifications as read
     * for a specific user.
     */
    List<Notification> findByUserIdAndIsReadFalse(Long userId);

    /*
     * Used when marking all notifications as read
     * for a specific user and branch.
     */
    List<Notification> findByUserIdAndBranchIdAndIsReadFalse(
            Long userId,
            Long branchId
    );
    

List<Notification> findByCampaign_IdAndCampaign_Admin_IdOrderByCreatedAtDesc(
        Long campaignId,
        Long adminId
);

}
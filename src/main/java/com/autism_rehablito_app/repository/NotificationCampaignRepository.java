
package com.autism_rehablito_app.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.autism_rehablito_app.dto.admin.AdminNotificationCampaignResponse;
import com.autism_rehablito_app.entity.NotificationCampaign;

public interface NotificationCampaignRepository
        extends JpaRepository<NotificationCampaign, Long> {

    /*
     * Get all campaigns created by a specific Admin.
     * Newest campaign first.
     */
    List<NotificationCampaign> findByAdminIdOrderByCreatedAtDesc(
            Long adminId
    );

    /*
     * Get a specific campaign created by a specific Admin.
     * Used for ownership validation.
     */
    Optional<NotificationCampaign> findByIdAndAdminId(
            Long campaignId,
            Long adminId
    );

    /*
     * Get campaigns for a specific branch.
     */
    List<NotificationCampaign> findByAdminIdAndBranchIdOrderByCreatedAtDesc(
            Long adminId,
            Long branchId
    );

    /*
     * Admin campaign history with individual recipient
     * read/unread statistics.
     */
    @Query("""
        SELECT new com.autism_rehablito_app.dto.admin.AdminNotificationCampaignResponse(
            c.id,
            c.title,
            c.message,
            c.type,
            c.recipient,
            c.branchId,
            c.totalRecipients,
            COALESCE(SUM(
                CASE WHEN n.isRead = true THEN 1 ELSE 0 END
            ), 0),
            COALESCE(SUM(
                CASE WHEN n.isRead = false THEN 1 ELSE 0 END
            ), 0),
            c.createdAt,
            c.status,
    		c.scheduledAt
        )
        FROM NotificationCampaign c
        LEFT JOIN Notification n ON n.campaign.id = c.id
        WHERE c.admin.id = :adminId
        GROUP BY
            c.id,
            c.title,
            c.message,
            c.type,
            c.recipient,
            c.branchId,
            c.totalRecipients,
            c.createdAt,
            c.status,
    		c.scheduledAt
        ORDER BY c.createdAt DESC
        """)
    List<AdminNotificationCampaignResponse> findCampaignHistoryWithStatistics(
            @Param("adminId") Long adminId
    );
    

@Query("""
    SELECT new com.autism_rehablito_app.dto.admin.AdminNotificationCampaignResponse(
        c.id,
        c.title,
        c.message,
        c.type,
        c.recipient,
        c.branchId,
        c.totalRecipients,
        COALESCE(SUM(CASE WHEN n.isRead = true THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN n.isRead = false THEN 1 ELSE 0 END), 0),
        c.createdAt,
        c.status,
		c.scheduledAt
    )
    FROM NotificationCampaign c
    LEFT JOIN Notification n ON n.campaign.id = c.id
    WHERE c.admin.id = :adminId
      AND (:branchId IS NULL OR c.branchId IS NULL OR c.branchId = :branchId)
      AND (:recipient IS NULL OR c.recipient = :recipient)
    GROUP BY
        c.id,
        c.title,
        c.message,
        c.type,
        c.recipient,
        c.branchId,
        c.totalRecipients,
        c.createdAt,
        c.status,
		c.scheduledAt
    HAVING
        (
            :status IS NULL
            OR :status = 'ALL'
            OR (
                :status = 'READ'
                AND COALESCE(SUM(CASE WHEN n.isRead = false THEN 1 ELSE 0 END), 0) = 0
            )
            OR (
                :status = 'UNREAD'
                AND COALESCE(SUM(CASE WHEN n.isRead = false THEN 1 ELSE 0 END), 0) > 0
            )
        )
    ORDER BY c.createdAt DESC
""")
List<AdminNotificationCampaignResponse> findCampaignHistoryWithFilters(
        @Param("adminId") Long adminId,
        @Param("branchId") Long branchId,
        @Param("recipient") String recipient,
        @Param("status") String status
);


List<NotificationCampaign> findTop50ByStatusAndScheduledAtLessThanEqualOrderByScheduledAtAsc(
        String status,
        LocalDateTime scheduledAt
);


@Modifying
@Query("""
    UPDATE NotificationCampaign c
    SET c.status = 'SENDING'
    WHERE c.id = :campaignId
      AND c.status = 'SCHEDULED'
      AND c.scheduledAt <= :currentTime
""")
int claimScheduledCampaign(
        @Param("campaignId") Long campaignId,
        @Param("currentTime") LocalDateTime currentTime
);


@Modifying
@Query("""
    UPDATE NotificationCampaign c
    SET c.status = 'FAILED',
        c.failureReason = :failureReason
    WHERE c.id = :campaignId
      AND c.status IN ('SENDING', 'SCHEDULED')
""")
int markCampaignAsFailed(
        @Param("campaignId") Long campaignId,
        @Param("failureReason") String failureReason
);





}

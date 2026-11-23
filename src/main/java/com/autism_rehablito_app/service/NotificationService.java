package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.NotificationMarkReadResponse;
import com.autism_rehablito_app.dto.NotificationResponse;
import com.autism_rehablito_app.dto.NotificationUnreadCountResponse;
import com.autism_rehablito_app.entity.NotificationType;
import com.autism_rehablito_app.dto.admin.AdminNotificationCampaignResponse;
import com.autism_rehablito_app.dto.admin.AdminNotificationRecipientResponse;
import com.autism_rehablito_app.dto.admin.AdminScheduleNotificationRequest;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(
            Long userId,
            Long branchId,
            String title,
            String message,
            NotificationType type,
            String referenceType,
            Long referenceId
    );
    


    /*
     * Admin notification distribution
     */
    List<NotificationResponse> createAdminNotifications(
            Long adminId,
            String title,
            String message,
            NotificationType type,
            String recipient,
            Long branchId
    );
    

    /*
     * Admin campaign history with read/unread statistics
     */
    List<AdminNotificationCampaignResponse> getAdminCampaignHistory(
            Long adminId
    );




    /*
     * User-wise notification APIs
     */
    List<NotificationResponse> getNotifications(Long userId);

    NotificationResponse getNotification(
            Long userId,
            Long notificationId
    );

    NotificationUnreadCountResponse getUnreadCount(Long userId);

    NotificationMarkReadResponse markAsRead(
            Long userId,
            Long notificationId
    );

    void markAllAsRead(Long userId);

    /*
     * Branch-aware internal methods.
     * These remain available for future branch-specific
     * operations.
     */
    List<NotificationResponse> getNotifications(
            Long userId,
            Long branchId
    );

    NotificationResponse getNotification(
            Long userId,
            Long branchId,
            Long notificationId
    );

    NotificationUnreadCountResponse getUnreadCount(
            Long userId,
            Long branchId
    );

    NotificationMarkReadResponse markAsRead(
            Long userId,
            Long branchId,
            Long notificationId
    );

    void markAllAsRead(
            Long userId,
            Long branchId
    );
    
    List<AdminNotificationCampaignResponse> getAdminCampaignHistoryWithFilters(
            Long adminId,
            Long branchId,
            String recipient,
            String status
    );
    
    List<AdminNotificationRecipientResponse> getCampaignRecipients(
            Long adminId,
            Long campaignId
    );
    
    Long scheduleAdminNotification(
            Long adminId,
            AdminScheduleNotificationRequest request
    );
    
    Long updateScheduledNotification(
            Long adminId,
            Long campaignId,
            AdminScheduleNotificationRequest request
    );
    
    void cancelScheduledNotification(Long adminId, Long campaignId);
}
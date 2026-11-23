package com.autism_rehablito_app.service;

import com.autism_rehablito_app.entity.NotificationType;

public interface NotificationEventService {

    void notifyUser(
            Long userId,
            Long branchId,
            String title,
            String message,
            NotificationType type,
            String referenceType,
            Long referenceId
    );

    void notifyAdmins(
            Long branchId,
            String title,
            String message,
            NotificationType type,
            String referenceType,
            Long referenceId
    );
}
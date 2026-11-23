package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.entity.NotificationType;
import com.autism_rehablito_app.entity.Role;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.repository.UserRepository;
import com.autism_rehablito_app.service.NotificationEventService;
import com.autism_rehablito_app.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationEventServiceImpl
        implements NotificationEventService {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationEventServiceImpl(
            NotificationService notificationService,
            UserRepository userRepository
    ) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @Override
    public void notifyUser(
            Long userId,
            Long branchId,
            String title,
            String message,
            NotificationType type,
            String referenceType,
            Long referenceId
    ) {
        notificationService.createNotification(
                userId,
                branchId,
                title,
                message,
                type,
                referenceType,
                referenceId
        );
    }

    @Override
    public void notifyAdmins(
            Long branchId,
            String title,
            String message,
            NotificationType type,
            String referenceType,
            Long referenceId
    ) {

        List<User> admins =
                userRepository.findByRoleAndActiveTrue(Role.ADMIN);

        for (User admin : admins) {

            notificationService.createNotification(
                    admin.getId(),
                    branchId,
                    title,
                    message,
                    type,
                    referenceType,
                    referenceId
            );
        }
    }
}
package com.autism_rehablito_app.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autism_rehablito_app.dto.NotificationMarkReadResponse;
import com.autism_rehablito_app.dto.NotificationResponse;
import com.autism_rehablito_app.dto.NotificationUnreadCountResponse;
import com.autism_rehablito_app.dto.admin.AdminNotificationCampaignResponse;
import com.autism_rehablito_app.dto.admin.AdminNotificationRecipientResponse;
import com.autism_rehablito_app.dto.admin.AdminScheduleNotificationRequest;
import com.autism_rehablito_app.entity.Branch;
import com.autism_rehablito_app.entity.Notification;
import com.autism_rehablito_app.entity.NotificationCampaign;
import com.autism_rehablito_app.entity.NotificationType;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.Role;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.exception.BadRequestException;
import com.autism_rehablito_app.exception.ResourceNotFoundException;
import com.autism_rehablito_app.repository.BranchRepository;
import com.autism_rehablito_app.repository.NotificationCampaignRepository;
import com.autism_rehablito_app.repository.NotificationRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.UserRepository;
import com.autism_rehablito_app.service.NotificationService;
import java.time.ZoneId;


@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    private final ParentRepository parentRepository;
    private final TherapistRepository therapistRepository;

    private final NotificationCampaignRepository campaignRepository;



    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            BranchRepository branchRepository,
            ParentRepository parentRepository,
            TherapistRepository therapistRepository,
            NotificationCampaignRepository campaignRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.parentRepository = parentRepository;
        this.therapistRepository = therapistRepository;
        this.campaignRepository = campaignRepository;
        
    }

    @Override
    public NotificationResponse createNotification(
            Long userId,
            Long branchId,
            String title,
            String message,
            NotificationType type,
            String referenceType,
            Long referenceId
    ) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        if (branchId == null) {
            throw new BadRequestException("Branch ID is required");
        }

        if (title == null || title.isBlank()) {
            throw new BadRequestException("Notification title is required");
        }

        if (message == null || message.isBlank()) {
            throw new BadRequestException("Notification message is required");
        }

        if (type == null) {
            throw new BadRequestException("Notification type is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId)
                );

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BadRequestException("User is inactive");
        }

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found with ID: " + branchId)
                );

        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new BadRequestException("Branch is inactive");
        }

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setBranch(branch);
        notification.setTitle(title.trim());
        notification.setMessage(message.trim());
        notification.setType(type);
        notification.setReferenceType(
                referenceType != null && !referenceType.isBlank()
                        ? referenceType.trim()
                        : null
        );
        notification.setReferenceId(referenceId);
        notification.setIsRead(false);

        Notification savedNotification =
                notificationRepository.save(notification);

        return mapToResponse(savedNotification);
    }
    


@Override
public List<NotificationResponse> createAdminNotifications(
        Long adminId,
        String title,
        String message,
        NotificationType type,
        String recipient,
        Long branchId
) {

    if (adminId == null) {
        throw new BadRequestException("Admin ID is required");
    }

    User admin = userRepository.findById(adminId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Admin not found")
            );

    if (!Boolean.TRUE.equals(admin.getActive())
            || admin.getRole() != com.autism_rehablito_app.entity.Role.ADMIN) {
        throw new BadRequestException("Invalid Admin account");
    }

    if (title == null || title.isBlank()) {
        throw new BadRequestException("Notification title is required");
    }

    if (title.trim().length() > 150) {
        throw new BadRequestException("Title cannot exceed 150 characters");
    }

    if (message == null || message.isBlank()) {
        throw new BadRequestException("Notification message is required");
    }

    if (message.trim().length() > 1000) {
        throw new BadRequestException("Message cannot exceed 1000 characters");
    }

    if (type == null) {
        throw new BadRequestException("Notification type is required");
    }

    if (recipient == null || recipient.isBlank()) {
        throw new BadRequestException("Recipient is required");
    }

    String normalizedRecipient = recipient.trim().toUpperCase();

    if (!normalizedRecipient.equals("PARENT")
            && !normalizedRecipient.equals("THERAPIST")
            && !normalizedRecipient.equals("BOTH")) {
        throw new BadRequestException(
                "Invalid recipient. Allowed: PARENT, THERAPIST, BOTH"
        );
    }

    if (branchId != null) {
        Branch selectedBranch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Branch not found with ID: " + branchId
                        )
                );

        if (!Boolean.TRUE.equals(selectedBranch.getActive())) {
            throw new BadRequestException("Selected branch is inactive");
        }
    }

    // Create campaign record
    NotificationCampaign campaign = new NotificationCampaign();
    campaign.setAdmin(admin);
    campaign.setTitle(title.trim());
    campaign.setMessage(message.trim());
    campaign.setType(type);
    campaign.setRecipient(normalizedRecipient);
    campaign.setBranchId(branchId);
    campaign.setTotalRecipients(0);

    campaign = campaignRepository.save(campaign);

    List<NotificationResponse> responses = new ArrayList<>();

    // Parent distribution
    if (normalizedRecipient.equals("PARENT")
            || normalizedRecipient.equals("BOTH")) {

        List<Parent> parents = branchId == null
                ? parentRepository.findByActiveTrue()
                : parentRepository.findByBranchIdAndActiveTrue(branchId);

        for (Parent parent : parents) {

            User user = parent.getUser();
            Branch branch = parent.getBranch();

            if (user == null || !Boolean.TRUE.equals(user.getActive())) {
                continue;
            }

            if (branch == null
                    || !Boolean.TRUE.equals(branch.getActive())) {
                throw new BadRequestException(
                        "Parent ID " + parent.getId()
                                + " has no valid active branch"
                );
            }

            if (branchId != null && !branchId.equals(branch.getId())) {
                continue;
            }

            Notification notification = createCampaignNotification(
                    user,
                    branch,
                    campaign,
                    title,
                    message,
                    type
            );

            Notification saved = notificationRepository.save(notification);
            responses.add(mapToResponse(saved));
        }
    }

    // Therapist distribution
    if (normalizedRecipient.equals("THERAPIST")
            || normalizedRecipient.equals("BOTH")) {

        List<Therapist> therapists = branchId == null
                ? therapistRepository.findByActiveTrue()
                : therapistRepository.findByBranchIdAndActiveTrue(branchId);

        for (Therapist therapist : therapists) {

            User user = therapist.getUser();
            Branch branch = therapist.getBranch();

            if (user == null || !Boolean.TRUE.equals(user.getActive())) {
                continue;
            }

            if (branch == null
                    || !Boolean.TRUE.equals(branch.getActive())) {
                throw new BadRequestException(
                        "Therapist ID " + therapist.getId()
                                + " has no valid active branch"
                );
            }

            if (branchId != null && !branchId.equals(branch.getId())) {
                continue;
            }

            Notification notification = createCampaignNotification(
                    user,
                    branch,
                    campaign,
                    title,
                    message,
                    type
            );

            Notification saved = notificationRepository.save(notification);
            responses.add(mapToResponse(saved));
        }
    }

    if (responses.isEmpty()) {
        throw new ResourceNotFoundException(
                "No active recipients found for the selected criteria"
        );
    }

    campaign.setTotalRecipients(responses.size());
    campaignRepository.save(campaign);

    return responses;
}


@Override
@Transactional(readOnly = true)
public List<AdminNotificationCampaignResponse> getAdminCampaignHistory(
        Long adminId
) {

    if (adminId == null) {
        throw new BadRequestException("Admin ID is required");
    }

    User admin = userRepository.findById(adminId)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Admin not found with ID: " + adminId
                    )
            );

    if (!Boolean.TRUE.equals(admin.getActive())) {
        throw new BadRequestException("Admin account is inactive");
    }

    if (admin.getRole() != Role.ADMIN) {
        throw new BadRequestException(
                "Only Admin can access notification campaign history"
        );
    }

    return campaignRepository
            .findCampaignHistoryWithStatistics(adminId);
}



private Notification createCampaignNotification(
        User user,
        Branch branch,
        NotificationCampaign campaign,
        String title,
        String message,
        NotificationType type
) {

    Notification notification = new Notification();

    notification.setUser(user);
    notification.setBranch(branch);
    notification.setCampaign(campaign);
    notification.setTitle(title.trim());
    notification.setMessage(message.trim());
    notification.setType(type);
    notification.setIsRead(false);

    return notification;
}



    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(
            Long userId,
            Long branchId
    ) {

        validateUserAndBranch(userId, branchId);

        return notificationRepository
                .findByUserIdAndBranchIdOrderByCreatedAtDesc(userId, branchId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotification(
            Long userId,
            Long branchId,
            Long notificationId
    ) {

        validateUserAndBranch(userId, branchId);

        if (notificationId == null) {
            throw new BadRequestException("Notification ID is required");
        }

        Notification notification =
                notificationRepository
                        .findByIdAndUserIdAndBranchId(
                                notificationId,
                                userId,
                                branchId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );

        return mapToResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationUnreadCountResponse getUnreadCount(
            Long userId,
            Long branchId
    ) {

        validateUserAndBranch(userId, branchId);

        long unreadCount =
                notificationRepository
                        .countByUserIdAndBranchIdAndIsReadFalse(
                                userId,
                                branchId
                        );

        return new NotificationUnreadCountResponse(unreadCount);
    }

    @Override
    public NotificationMarkReadResponse markAsRead(
            Long userId,
            Long branchId,
            Long notificationId
    ) {

        validateUserAndBranch(userId, branchId);

        if (notificationId == null) {
            throw new BadRequestException("Notification ID is required");
        }

        Notification notification =
                notificationRepository
                        .findByIdAndUserIdAndBranchId(
                                notificationId,
                                userId,
                                branchId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );

        if (!Boolean.TRUE.equals(notification.getIsRead())) {

            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));

            notificationRepository.save(notification);
        }

        return new NotificationMarkReadResponse(
                notification.getId(),
                notification.getIsRead(),
                notification.getReadAt()
        );
    }

    @Override
    public void markAllAsRead(
            Long userId,
            Long branchId
    ) {

        validateUserAndBranch(userId, branchId);

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdAndBranchIdAndIsReadFalse(
                                userId,
                                branchId
                        );

        LocalDateTime now = LocalDateTime.now(
                ZoneId.of("Asia/Kolkata")
        );

        for (Notification notification : notifications) {

            notification.setIsRead(true);
            notification.setReadAt(now);
        }

        if (!notifications.isEmpty()) {
            notificationRepository.saveAll(notifications);
        }
    }

    private void validateUserAndBranch(
            Long userId,
            Long branchId
    ) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        if (branchId == null) {
            throw new BadRequestException("Branch ID is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BadRequestException("User is inactive");
        }

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Branch not found with ID: " + branchId
                        )
                );

        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new BadRequestException("Branch is inactive");
        }
    }

    private NotificationResponse mapToResponse(
            Notification notification
    ) {

        return new NotificationResponse(
                notification.getId(),
                notification.getBranch().getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getReferenceType(),
                notification.getReferenceId(),
                notification.getIsRead(),
                notification.getCreatedAt(),
                notification.getReadAt()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(Long userId) {

        validateUser(userId);

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotification(
            Long userId,
            Long notificationId
    ) {

        validateUser(userId);

        if (notificationId == null) {
            throw new BadRequestException("Notification ID is required");
        }

        Notification notification =
                notificationRepository
                        .findByIdAndUserId(notificationId, userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );

        return mapToResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationUnreadCountResponse getUnreadCount(Long userId) {

        validateUser(userId);

        long unreadCount =
                notificationRepository
                        .countByUserIdAndIsReadFalse(userId);

        return new NotificationUnreadCountResponse(unreadCount);
    }

    @Override
    public NotificationMarkReadResponse markAsRead(
            Long userId,
            Long notificationId
    ) {

        validateUser(userId);

        if (notificationId == null) {
            throw new BadRequestException("Notification ID is required");
        }

        Notification notification =
                notificationRepository
                        .findByIdAndUserId(notificationId, userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                )
                        );

        if (!Boolean.TRUE.equals(notification.getIsRead())) {

            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));

            notificationRepository.save(notification);
        }

        return new NotificationMarkReadResponse(
                notification.getId(),
                notification.getIsRead(),
                notification.getReadAt()
        );
    }

    @Override
    public void markAllAsRead(Long userId) {

        validateUser(userId);

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdAndIsReadFalse(userId);

        if (notifications.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now(
                ZoneId.of("Asia/Kolkata")
        );

        for (Notification notification : notifications) {
            notification.setIsRead(true);
            notification.setReadAt(now);
        }

        notificationRepository.saveAll(notifications);
    }
    
    private void validateUser(Long userId) {

        if (userId == null) {
            throw new BadRequestException("User ID is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BadRequestException("User is inactive");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AdminNotificationCampaignResponse> getAdminCampaignHistoryWithFilters(
            Long adminId,
            Long branchId,
            String recipient,
            String status
    ) {
        if (adminId == null) {
            throw new IllegalArgumentException("Admin ID is required.");
        }

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found."));

        if (!admin.getActive() || admin.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only active admins can access campaign history.");
        }

        if (branchId != null && !branchRepository.existsById(branchId)) {
            throw new ResourceNotFoundException("Branch not found.");
        }

        if (recipient != null && !recipient.isBlank()) {
            recipient = recipient.trim().toUpperCase();

            if (!List.of("PARENT", "THERAPIST", "BOTH").contains(recipient)) {
                throw new IllegalArgumentException("Invalid recipient filter.");
            }
        } else {
            recipient = null;
        }

        if (status != null && !status.isBlank()) {
            status = status.trim().toUpperCase();

            if (!List.of("ALL", "READ", "UNREAD").contains(status)) {
                throw new IllegalArgumentException("Invalid status filter.");
            }
        } else {
            status = "ALL";
        }

        return campaignRepository.findCampaignHistoryWithFilters(
                adminId,
                branchId,
                recipient,
                status
        );
    }
    

@Override
@Transactional(readOnly = true)
public List<AdminNotificationRecipientResponse> getCampaignRecipients(
        Long adminId,
        Long campaignId) {

    if (adminId == null || campaignId == null) {
        throw new IllegalArgumentException(
                "Admin ID and Campaign ID are required."
        );
    }

    User admin = userRepository.findById(adminId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Admin not found.")
            );

    if (!admin.getActive() || admin.getRole() != Role.ADMIN) {
        throw new AccessDeniedException(
                "Only active admins can access campaign recipients."
        );
    }

    List<Notification> notifications =
            notificationRepository
                    .findByCampaign_IdAndCampaign_Admin_IdOrderByCreatedAtDesc(
                            campaignId,
                            adminId
                    );

    return notifications.stream()
            .map(notification -> {

                User recipient = notification.getUser();

                Branch branch = notification.getBranch();

                return new AdminNotificationRecipientResponse(
                        notification.getId(),
                        recipient.getId(),
                        recipient.getName(),
                        recipient.getRole().name(),
                        branch != null ? branch.getId() : null,
                        branch != null ? branch.getBranchName() : null,
                        notification.getIsRead(),
                        notification.getCreatedAt(),
                        notification.getReadAt()
                );
            })
            .toList();
}


@Override
@Transactional
public Long scheduleAdminNotification(
        Long adminId,
        AdminScheduleNotificationRequest request) {

    if (adminId == null || request == null) {
        throw new IllegalArgumentException(
                "Admin ID and request are required."
        );
    }

    User admin = userRepository.findById(adminId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Admin not found.")
            );

    if (!admin.getActive() || admin.getRole() != Role.ADMIN) {
        throw new AccessDeniedException(
                "Only active admins can schedule notifications."
        );
    }

    if (request.getScheduledAt() == null
            || !request.getScheduledAt().isAfter(
                    LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
            		)) {
        throw new IllegalArgumentException(
                "Scheduled date and time must be in the future."
        );
    }

    if (request.getRecipient() == null
            || !List.of("PARENT", "THERAPIST", "BOTH")
                    .contains(request.getRecipient().trim().toUpperCase())) {
        throw new IllegalArgumentException(
                "Invalid notification recipient."
        );
    }

    if (request.getBranchId() != null
            && !branchRepository.existsById(request.getBranchId())) {
        throw new IllegalArgumentException("Branch not found.");
    }

    NotificationCampaign campaign = new NotificationCampaign();

    campaign.setAdmin(admin);
    campaign.setTitle(request.getTitle().trim());
    campaign.setMessage(request.getMessage().trim());
    campaign.setType(request.getType());
    campaign.setRecipient(
            request.getRecipient().trim().toUpperCase()
    );
    campaign.setBranchId(request.getBranchId());
    campaign.setTotalRecipients(0);
    campaign.setScheduledAt(request.getScheduledAt());
    campaign.setStatus("SCHEDULED");
    campaign.setSentAt(null);

    NotificationCampaign savedCampaign =
            campaignRepository.save(campaign);

    return savedCampaign.getId();
}


@Override
@Transactional
public Long updateScheduledNotification(
        Long adminId,
        Long campaignId,
        AdminScheduleNotificationRequest request) {

    if (adminId == null || campaignId == null || request == null) {
        throw new IllegalArgumentException(
                "Admin ID, Campaign ID and request are required."
        );
    }

    User admin = userRepository.findById(adminId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Admin not found.")
            );

    if (!admin.getActive() || admin.getRole() != Role.ADMIN) {
        throw new AccessDeniedException(
                "Only active admins can update scheduled notifications."
        );
    }

    NotificationCampaign campaign =
            campaignRepository.findByIdAndAdminId(
                    campaignId, adminId
            ).orElseThrow(() ->
                    new IllegalArgumentException(
                            "Campaign not found."
                    )
            );

    if (!"SCHEDULED".equals(campaign.getStatus())) {
        throw new IllegalStateException(
                "Only scheduled campaigns can be updated."
        );
    }

    if (request.getScheduledAt() == null
            || !request.getScheduledAt().isAfter(
                    LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
            		)) {
        throw new IllegalArgumentException(
                "Scheduled date and time must be in the future."
        );
    }

    if (request.getTitle() == null
            || request.getTitle().isBlank()
            || request.getTitle().length() > 150) {
        throw new IllegalArgumentException("Invalid notification title.");
    }

    if (request.getMessage() == null
            || request.getMessage().isBlank()
            || request.getMessage().length() > 1000) {
        throw new IllegalArgumentException("Invalid notification message.");
    }

    if (request.getType() == null) {
        throw new IllegalArgumentException(
                "Notification type is required."
        );
    }

    if (request.getRecipient() == null
            || !List.of("PARENT", "THERAPIST", "BOTH")
                    .contains(request.getRecipient().trim().toUpperCase())) {
        throw new IllegalArgumentException(
                "Invalid notification recipient."
        );
    }

    if (request.getBranchId() != null
            && !branchRepository.existsById(request.getBranchId())) {
        throw new IllegalArgumentException("Branch not found.");
    }

    campaign.setTitle(request.getTitle().trim());
    campaign.setMessage(request.getMessage().trim());
    campaign.setType(request.getType());
    campaign.setRecipient(
            request.getRecipient().trim().toUpperCase()
    );
    campaign.setBranchId(request.getBranchId());
    campaign.setScheduledAt(request.getScheduledAt());

    NotificationCampaign updatedCampaign =
            campaignRepository.save(campaign);

    return updatedCampaign.getId();
}


@Override
@Transactional
public void cancelScheduledNotification(
        Long adminId,
        Long campaignId) {

    if (adminId == null || campaignId == null) {
        throw new IllegalArgumentException(
                "Admin ID and Campaign ID are required."
        );
    }

    User admin = userRepository.findById(adminId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Admin not found.")
            );

    if (!admin.getActive() || admin.getRole() != Role.ADMIN) {
        throw new AccessDeniedException(
                "Only active admins can cancel scheduled notifications."
        );
    }

    NotificationCampaign campaign =
            campaignRepository.findByIdAndAdminId(
                    campaignId,
                    adminId
            ).orElseThrow(() ->
                    new IllegalArgumentException("Campaign not found.")
            );

    if (!"SCHEDULED".equals(campaign.getStatus())) {
        throw new IllegalStateException(
                "Only scheduled campaigns can be cancelled."
        );
    }

    campaign.setStatus("CANCELLED");

    campaignRepository.save(campaign);
}




}
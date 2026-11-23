
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.entity.*;
import com.autism_rehablito_app.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.ZoneId;

@Service
public class ScheduledNotificationProcessor {

    private final NotificationCampaignRepository campaignRepository;
    private final NotificationRepository notificationRepository;
    private final ParentRepository parentRepository;
    private final TherapistRepository therapistRepository;

    public ScheduledNotificationProcessor(
            NotificationCampaignRepository campaignRepository,
            NotificationRepository notificationRepository,
            ParentRepository parentRepository,
            TherapistRepository therapistRepository) {

        this.campaignRepository = campaignRepository;
        this.notificationRepository = notificationRepository;
        this.parentRepository = parentRepository;
        this.therapistRepository = therapistRepository;
    }

    @Transactional
    public void processCampaign(Long campaignId) {

    	LocalDateTime now = LocalDateTime.now(
    	        ZoneId.of("Asia/Kolkata")
    	);

        // Atomically claim campaign.
        int claimed = campaignRepository.claimScheduledCampaign(
                campaignId,
                now
        );

        if (claimed == 0) {
            return;
        }

        NotificationCampaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Scheduled campaign not found."
                                )
                        );

        List<Notification> notifications = new ArrayList<>();

        String recipient = campaign.getRecipient();

        Long branchId = campaign.getBranchId();

        // Parent recipients
        if ("PARENT".equals(recipient) || "BOTH".equals(recipient)) {

            List<Parent> parents = branchId == null
                    ? parentRepository.findByActiveTrue()
                    : parentRepository.findByBranchIdAndActiveTrue(branchId);

            for (Parent parent : parents) {

                User user = parent.getUser();
                Branch branch = parent.getBranch();

                if (user == null
                        || !Boolean.TRUE.equals(user.getActive())) {
                    continue;
                }

                if (branch == null
                        || !Boolean.TRUE.equals(branch.getActive())) {
                    continue;
                }

                if (branchId != null
                        && !branchId.equals(branch.getId())) {
                    continue;
                }

                notifications.add(
                        createNotification(campaign, user, branch)
                );
            }
        }

        // Therapist recipients
        if ("THERAPIST".equals(recipient) || "BOTH".equals(recipient)) {

            List<Therapist> therapists = branchId == null
                    ? therapistRepository.findByActiveTrue()
                    : therapistRepository.findByBranchIdAndActiveTrue(branchId);

            for (Therapist therapist : therapists) {

                User user = therapist.getUser();
                Branch branch = therapist.getBranch();

                if (user == null
                        || !Boolean.TRUE.equals(user.getActive())) {
                    continue;
                }

                if (branch == null
                        || !Boolean.TRUE.equals(branch.getActive())) {
                    continue;
                }

                if (branchId != null
                        && !branchId.equals(branch.getId())) {
                    continue;
                }

                notifications.add(
                        createNotification(campaign, user, branch)
                );
            }
        }

        // No recipients found
     // No recipients found
        if (notifications.isEmpty()) {

            campaign.setTotalRecipients(0);

            campaign.setStatus("FAILED");

            campaign.setFailureReason(
                    "No active eligible recipients found for this campaign."
            );

            campaignRepository.save(campaign);

            return;
        }

        // Save individual notifications
        notificationRepository.saveAll(notifications);

        campaign.setTotalRecipients(notifications.size());
        campaign.setFailureReason(null);
        campaign.setStatus("SENT");
        campaign.setSentAt(
                LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
        );

        campaignRepository.save(campaign);
    }

    private Notification createNotification(
            NotificationCampaign campaign,
            User user,
            Branch branch) {

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setBranch(branch);
        notification.setCampaign(campaign);
        notification.setTitle(campaign.getTitle());
        notification.setMessage(campaign.getMessage());
        notification.setType(campaign.getType());
        notification.setIsRead(false);

        return notification;
    }
}

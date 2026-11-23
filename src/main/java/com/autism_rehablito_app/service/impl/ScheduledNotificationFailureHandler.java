
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.repository.NotificationCampaignRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduledNotificationFailureHandler {

    private final NotificationCampaignRepository campaignRepository;

    public ScheduledNotificationFailureHandler(
            NotificationCampaignRepository campaignRepository) {

        this.campaignRepository = campaignRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCampaignAsFailed(
            Long campaignId,
            String failureReason) {

        String reason = failureReason;

        if (reason == null || reason.isBlank()) {
            reason = "Unknown error during scheduled notification processing.";
        }

        // Database column supports maximum 1000 characters.
        if (reason.length() > 1000) {
            reason = reason.substring(0, 1000);
        }

        campaignRepository.markCampaignAsFailed(
                campaignId,
                reason
        );
    }
}

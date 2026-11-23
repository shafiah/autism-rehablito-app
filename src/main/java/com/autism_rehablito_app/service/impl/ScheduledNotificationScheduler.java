
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.entity.NotificationCampaign;
import com.autism_rehablito_app.repository.NotificationCampaignRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ScheduledNotificationScheduler {

    private static final ZoneId INDIA_ZONE =
            ZoneId.of("Asia/Kolkata");

    private final NotificationCampaignRepository campaignRepository;
    private final ScheduledNotificationProcessor processor;
    private final ScheduledNotificationFailureHandler failureHandler;

    public ScheduledNotificationScheduler(
            NotificationCampaignRepository campaignRepository,
            ScheduledNotificationProcessor processor,
            ScheduledNotificationFailureHandler failureHandler) {

        this.campaignRepository = campaignRepository;
        this.processor = processor;
        this.failureHandler = failureHandler;
    }

    @Scheduled(fixedDelay = 30000)
    public void processDueCampaigns() {

        LocalDateTime currentTime =
                LocalDateTime.now(INDIA_ZONE);

        List<NotificationCampaign> campaigns =
                campaignRepository
                        .findTop50ByStatusAndScheduledAtLessThanEqualOrderByScheduledAtAsc(
                                "SCHEDULED",
                                currentTime
                        );

        for (NotificationCampaign campaign : campaigns) {

            try {

                processor.processCampaign(campaign.getId());

            } catch (Exception exception) {

                String failureReason =
                        exception.getMessage();

                if (failureReason == null || failureReason.isBlank()) {
                    failureReason = exception.getClass().getSimpleName();
                } else {
                    failureReason =
                            exception.getClass().getSimpleName()
                                    + ": " + failureReason;
                }

                try {

                    failureHandler.markCampaignAsFailed(
                            campaign.getId(),
                            failureReason
                    );

                } catch (Exception failureException) {

                    System.err.println(
                            "Unable to save campaign failure. Campaign ID: "
                                    + campaign.getId()
                                    + ", Error: "
                                    + failureException.getMessage()
                    );
                }

                System.err.println(
                        "Scheduled campaign processing failed. Campaign ID: "
                                + campaign.getId()
                                + ", Error: "
                                + failureReason
                );
            }
        }
    }
}

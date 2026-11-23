package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.therapist.TherapistReportCreateRequest;
import com.autism_rehablito_app.dto.therapist.TherapistReportResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapist;
import com.autism_rehablito_app.entity.ReportStatus;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.TherapyReport;
import com.autism_rehablito_app.exception.BadRequestException;
import com.autism_rehablito_app.exception.ResourceNotFoundException;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ChildTherapistRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.TherapyReportRepository;
import com.autism_rehablito_app.service.TherapistReportService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autism_rehablito_app.entity.NotificationType;
import com.autism_rehablito_app.service.NotificationEventService;

@Service
public class TherapistReportServiceImpl
        implements TherapistReportService {

    private final TherapyReportRepository therapyReportRepository;
    private final TherapistRepository therapistRepository;
    private final ChildRepository childRepository;
    private final ChildTherapistRepository childTherapistRepository;
    private final NotificationEventService notificationEventService;

    public TherapistReportServiceImpl(
            TherapyReportRepository therapyReportRepository,
            TherapistRepository therapistRepository,
            ChildRepository childRepository,
            ChildTherapistRepository childTherapistRepository,
            NotificationEventService notificationEventService) {

        this.therapyReportRepository = therapyReportRepository;
        this.therapistRepository = therapistRepository;
        this.childRepository = childRepository;
        this.childTherapistRepository = childTherapistRepository;
        this.notificationEventService = notificationEventService;
    }

    @Override
    @Transactional
    public TherapistReportResponse createDraftReport(
            Long userId,
            Long childId,
            TherapistReportCreateRequest request) {

        Therapist therapist =
                therapistRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Therapist profile not found"
                                ));

        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(
                therapist.getUser().getActive())) {

            throw new IllegalArgumentException(
                    "Therapist account is inactive"
            );
        }

        Child child =
                childRepository.findById(childId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Child not found"
                                ));

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        /*
         * Therapist must have an active assignment
         * with this child.
         */
        ChildTherapist assignment =
                childTherapistRepository
                        .findByChildIdAndTherapistIdAndActive(
                                childId,
                                therapist.getId(),
                                true
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Therapist is not assigned to this child"
                                ));

        TherapyReport report =
                new TherapyReport();

        report.setChild(child);
        report.setTherapist(therapist);

        report.setReportTitle(
                request.getReportTitle()
        );

        report.setReportContent(
                request.getReportContent()
        );

        /*
         * Every newly created report starts as DRAFT.
         */
        report.setStatus(ReportStatus.DRAFT);

        TherapyReport savedReport =
                therapyReportRepository.save(report);

        return mapToResponse(savedReport);
    }

    private TherapistReportResponse mapToResponse(
            TherapyReport report) {

        return new TherapistReportResponse(
                report.getId(),

                report.getChild().getId(),
                report.getChild().getChildName(),

                report.getTherapist().getId(),
                report.getTherapist()
                        .getUser()
                        .getName(),

                report.getReportTitle(),
                report.getReportContent(),

                report.getStatus(),

                report.getAdminRemarks(),

                report.getCreatedAt(),
                report.getUpdatedAt(),
                report.getSubmittedAt(),
                report.getReviewedAt(),
                report.getPublishedAt()
        );
    }
    
    @Override
    public TherapistReportResponse submitReport(Long userId, Long reportId) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Therapist not found"));

        if (!therapist.getActive()) {
            throw new BadRequestException("Therapist account is inactive");
        }

        if (!therapist.getUser().getActive()) {
            throw new BadRequestException("Therapist user account is inactive");
        }

        TherapyReport report = therapyReportRepository.findById(reportId)
                .orElseThrow(() ->
                        new BadRequestException("Report not found"));

        if (!report.getTherapist().getId().equals(therapist.getId())) {
            throw new BadRequestException(
                    "You are not authorized to submit this report"
            );
        }

        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new BadRequestException(
                    "Only DRAFT reports can be submitted"
            );
        }

        report.setStatus(ReportStatus.SUBMITTED);
        report.setSubmittedAt(LocalDateTime.now());

        TherapyReport savedReport = therapyReportRepository.save(report);
        
        notificationEventService.notifyUser(
                report.getChild().getParent().getUser().getId(),
                report.getChild().getBranch().getId(),
                "Report Published",
                "A new therapy report for "
                        + report.getChild().getChildName()
                        + " has been published.",
                NotificationType.REPORT,
                "REPORT",
                report.getId()
        );

        return mapToResponse(savedReport);
    }
    

    // =====================================================
    // GET REPORTS FOR AUTHENTICATED THERAPIST
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<TherapistReportResponse> getMyReports(Long userId) {

        Therapist therapist =
                therapistRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Therapist not found"
                                ));

        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(
                        therapist.getUser().getActive())) {

            throw new BadRequestException(
                    "Therapist account is inactive"
            );
        }

        return therapyReportRepository
                .findByTherapistIdOrderByCreatedAtDesc(
                        therapist.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

}
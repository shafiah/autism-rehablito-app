package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.parent.ParentReportResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.ReportStatus;
import com.autism_rehablito_app.entity.TherapyReport;
import com.autism_rehablito_app.exception.BadRequestException;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.repository.TherapyReportRepository;
import com.autism_rehablito_app.service.ParentReportService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ParentReportServiceImpl implements ParentReportService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final TherapyReportRepository therapyReportRepository;

    public ParentReportServiceImpl(
            ParentRepository parentRepository,
            ChildRepository childRepository,
            TherapyReportRepository therapyReportRepository) {

        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.therapyReportRepository = therapyReportRepository;
    }

    @Override
    public List<ParentReportResponse> getMyChildReports(
            Long userId,
            Long childId) {

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Parent profile not found"));

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException("Child not found"));

        validateParentChildAccess(parent, child);

        List<TherapyReport> reports =
                therapyReportRepository.findByChildIdAndStatus(
                        childId,
                        ReportStatus.PUBLISHED
                );

        return reports.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ParentReportResponse getMyChildReportById(
            Long userId,
            Long childId,
            Long reportId) {

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Parent profile not found"));

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException("Child not found"));

        validateParentChildAccess(parent, child);

        TherapyReport report =
                therapyReportRepository.findById(reportId)
                        .orElseThrow(() ->
                                new BadRequestException("Report not found"));

        if (!report.getChild().getId().equals(childId)) {
            throw new BadRequestException(
                    "Report does not belong to this child"
            );
        }

        if (report.getStatus() != ReportStatus.PUBLISHED) {
            throw new BadRequestException(
                    "Report is not published"
            );
        }

        return mapToResponse(report);
    }

    private void validateParentChildAccess(
            Parent parent,
            Child child) {

        if (!child.getParent().getId().equals(parent.getId())) {
            throw new BadRequestException(
                    "You are not authorized to access this child"
            );
        }

        if (!child.getActive()) {
            throw new BadRequestException(
                    "Child account is inactive"
            );
        }
    }

    private ParentReportResponse mapToResponse(
            TherapyReport report) {

        return new ParentReportResponse(
                report.getId(),
                report.getChild().getId(),
                report.getChild().getChildName(),
                report.getTherapist().getId(),
                report.getTherapist().getUser().getName(),
                report.getReportTitle(),
                report.getReportContent(),
                report.getStatus(),
                report.getCreatedAt(),
                report.getSubmittedAt(),
                report.getReviewedAt(),
                report.getPublishedAt()
        );
    }
}

package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.admin.AdminReportResponse;
import com.autism_rehablito_app.dto.admin.AdminReportReviewRequest;
import com.autism_rehablito_app.entity.TherapyReport;
import com.autism_rehablito_app.entity.ReportStatus;
import com.autism_rehablito_app.exception.BadRequestException;
import com.autism_rehablito_app.repository.TherapyReportRepository;
import com.autism_rehablito_app.service.AdminReportService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminReportServiceImpl implements AdminReportService {

    private final TherapyReportRepository therapyReportRepository;

    public AdminReportServiceImpl(
            TherapyReportRepository therapyReportRepository) {

        this.therapyReportRepository = therapyReportRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminReportResponse> getReportsByStatus(
            ReportStatus status) {

        if (status == null) {
            throw new BadRequestException(
                    "Report status is required");
        }

        return therapyReportRepository
                .findByStatusOrderBySubmittedAtDesc(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminReportResponse reviewReport(
            Long reportId,
            AdminReportReviewRequest request) {

        TherapyReport report = therapyReportRepository.findById(reportId)
                .orElseThrow(() ->
                        new BadRequestException("Report not found"));

        if (report.getStatus() != ReportStatus.SUBMITTED) {
            throw new BadRequestException(
                    "Only SUBMITTED reports can be reviewed");
        }

        report.setAdminRemarks(request.getAdminRemarks());
        report.setStatus(ReportStatus.REVIEWED);
        report.setReviewedAt(LocalDateTime.now());

        TherapyReport savedReport =
                therapyReportRepository.save(report);

        return mapToResponse(savedReport);
    }

    @Override
    public AdminReportResponse publishReport(Long reportId) {

        TherapyReport report = therapyReportRepository.findById(reportId)
                .orElseThrow(() ->
                        new BadRequestException("Report not found"));

        if (report.getStatus() != ReportStatus.REVIEWED) {
            throw new BadRequestException(
                    "Only REVIEWED reports can be published");
        }

        report.setStatus(ReportStatus.PUBLISHED);
        report.setPublishedAt(LocalDateTime.now());

        TherapyReport savedReport =
                therapyReportRepository.save(report);

        return mapToResponse(savedReport);
    }


private AdminReportResponse mapToResponse(
        TherapyReport report) {

    AdminReportResponse response = new AdminReportResponse(
            report.getId(),
            report.getChild().getId(),
            report.getChild().getChildName(),
            report.getTherapist().getId(),
            report.getTherapist().getUser().getName(),
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

    response.setBranchId(
            report.getChild().getBranch().getId());

    response.setBranchName(
            report.getChild().getBranch().getBranchName());

    response.setTherapyType(
            report.getTherapist().getTherapyType());

    return response;
}

}


package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.admin.AdminReportResponse;
import com.autism_rehablito_app.dto.admin.AdminReportReviewRequest;
import com.autism_rehablito_app.entity.ReportStatus;

import java.util.List;

public interface AdminReportService {

    List<AdminReportResponse> getReportsByStatus(
            ReportStatus status
    );

    AdminReportResponse reviewReport(
            Long reportId,
            AdminReportReviewRequest request
    );

    AdminReportResponse publishReport(Long reportId);
}

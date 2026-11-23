package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.parent.ParentReportResponse;

import java.util.List;

public interface ParentReportService {

    List<ParentReportResponse> getMyChildReports(
            Long userId,
            Long childId
    );

    ParentReportResponse getMyChildReportById(
            Long userId,
            Long childId,
            Long reportId
    );
}
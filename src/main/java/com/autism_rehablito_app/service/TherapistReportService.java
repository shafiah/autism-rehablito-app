package com.autism_rehablito_app.service;

import java.util.List;

import com.autism_rehablito_app.dto.therapist.TherapistReportCreateRequest;
import com.autism_rehablito_app.dto.therapist.TherapistReportResponse;

public interface TherapistReportService {

    TherapistReportResponse createDraftReport(
            Long userId,
            Long childId,
            TherapistReportCreateRequest request
    );

    TherapistReportResponse submitReport(
            Long userId,
            Long reportId
    );
    

    List<TherapistReportResponse> getMyReports(Long userId);

}
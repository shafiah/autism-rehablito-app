package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.ReportStatus;
import com.autism_rehablito_app.entity.TherapyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TherapyReportRepository
        extends JpaRepository<TherapyReport, Long> {

    List<TherapyReport> findByChildId(Long childId);

    List<TherapyReport> findByTherapistId(Long therapistId);

    List<TherapyReport> findByChildIdAndStatus(
            Long childId,
            ReportStatus status
    );
    

    List<TherapyReport> findByTherapistIdOrderByCreatedAtDesc(Long therapistId);
    
    List<TherapyReport> findByStatusOrderBySubmittedAtDesc(
            ReportStatus status
    );

}
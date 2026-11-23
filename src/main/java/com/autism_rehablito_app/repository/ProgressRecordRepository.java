package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.ProgressRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressRecordRepository
        extends JpaRepository<ProgressRecord, Long> {

    List<ProgressRecord> findByChildId(Long childId);

    List<ProgressRecord> findByTherapistId(Long therapistId);

    List<ProgressRecord> findBySessionId(Long sessionId);

    Optional<ProgressRecord> findBySessionIdAndTherapistId(
            Long sessionId,
            Long therapistId
    );
    

    boolean existsBySessionId(Long sessionId);
    
    List<ProgressRecord> findByChildIdAndTherapistIdOrderByCreatedAtDesc(
            Long childId,
            Long therapistId
    );
    
    List<ProgressRecord> findByChildIdOrderByCreatedAtDesc(
            Long childId
    );
}
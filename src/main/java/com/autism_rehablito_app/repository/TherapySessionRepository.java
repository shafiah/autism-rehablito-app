package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.TherapySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TherapySessionRepository
        extends JpaRepository<TherapySession, Long> {

    List<TherapySession> findByBranchId(Long branchId);

    List<TherapySession> findByTherapistId(Long therapistId);

    List<TherapySession> findByChildId(Long childId);
}
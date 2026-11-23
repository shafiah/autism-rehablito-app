
package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.Homework;
import com.autism_rehablito_app.entity.HomeworkStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeworkRepository
        extends JpaRepository<Homework, Long> {

    // Existing Therapist Homework queries

    List<Homework> findByChildId(Long childId);

    List<Homework> findByTherapistId(Long therapistId);

    List<Homework> findByChildIdAndStatus(
            Long childId,
            HomeworkStatus status
    );

    List<Homework> findByTherapistIdAndStatus(
            Long therapistId,
            HomeworkStatus status
    );

    List<Homework> findBySessionId(Long sessionId);


    // Admin Homework filters

    @Query("""
            SELECT h FROM Homework h
            JOIN h.child c
            LEFT JOIN h.session s
            WHERE (:branchId IS NULL OR c.branch.id = :branchId)
              AND (:childId IS NULL OR c.id = :childId)
              AND (:status IS NULL OR h.status = :status)
              AND (:therapyType IS NULL OR
    		LOWER(h.therapyType) = LOWER(:therapyType))
            ORDER BY h.createdAt DESC
            """)
    List<Homework> findAdminHomework(
            @Param("branchId") Long branchId,
            @Param("childId") Long childId,
            @Param("status") HomeworkStatus status,
            @Param("therapyType") String therapyType
    );
}

package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.ChildTherapist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChildTherapistRepository
        extends JpaRepository<ChildTherapist, Long> {

    List<ChildTherapist> findByChildId(Long childId);

    List<ChildTherapist> findByTherapistId(Long therapistId);

    List<ChildTherapist> findByChildIdAndActive(
            Long childId,
            Boolean active
    );

    Optional<ChildTherapist> findByChildIdAndTherapistId(
            Long childId,
            Long therapistId
    );

    Optional<ChildTherapist> findByChildIdAndTherapistIdAndActive(
            Long childId,
            Long therapistId,
            Boolean active
    );
}
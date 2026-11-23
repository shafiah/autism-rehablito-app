
package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.ChildTherapy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildTherapyRepository
        extends JpaRepository<ChildTherapy, Long> {

    List<ChildTherapy> findByChildId(Long childId);

    List<ChildTherapy> findByChildIdAndActiveTrue(Long childId);

    boolean existsByChildIdAndTherapyType(
            Long childId,
            String therapyType
    );
}

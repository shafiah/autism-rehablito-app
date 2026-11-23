
package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {

    List<Child> findByParentId(Long parentId);

    List<Child> findByBranchId(Long branchId);

    List<Child> findByBranchIdAndActive(
            Long branchId,
            Boolean active
    );

    boolean existsByParentIdAndActiveTrue(Long parentId);
}

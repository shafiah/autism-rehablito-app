
package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.Parent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    Optional<Parent> findByUserId(Long userId);

    // Find active parents belonging to a selected branch
    List<Parent> findByBranchIdAndActiveTrue(Long branchId);

    // Find all active parents across all branches
    List<Parent> findByActiveTrue();
}

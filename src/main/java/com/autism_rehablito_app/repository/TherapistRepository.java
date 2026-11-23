
package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.Therapist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TherapistRepository extends JpaRepository<Therapist, Long> {

    Optional<Therapist> findByUserId(Long userId);

    // Existing branch-wise query
    List<Therapist> findByBranchId(Long branchId);

    // Find active therapists belonging to a selected branch
    List<Therapist> findByBranchIdAndActiveTrue(Long branchId);

    // Find all active therapists across all branches
    List<Therapist> findByActiveTrue();
}

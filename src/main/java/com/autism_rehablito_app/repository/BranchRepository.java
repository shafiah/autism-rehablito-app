package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findByBranchName(String branchName);
    
    Optional<Branch> findByBranchNameAndAddress(
            String branchName,
            String address
    );
}
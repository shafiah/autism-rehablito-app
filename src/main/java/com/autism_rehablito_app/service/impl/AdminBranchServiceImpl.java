package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.admin.AdminBranchRequest;
import com.autism_rehablito_app.dto.admin.AdminBranchResponse;
import com.autism_rehablito_app.entity.Branch;
import com.autism_rehablito_app.repository.BranchRepository;
import com.autism_rehablito_app.service.AdminBranchService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminBranchServiceImpl
        implements AdminBranchService {

    private final BranchRepository branchRepository;

    public AdminBranchServiceImpl(
            BranchRepository branchRepository) {

        this.branchRepository = branchRepository;
    }

    @Override
    @Transactional
    public AdminBranchResponse addBranch(
            AdminBranchRequest request) {

        String branchName =
                request.getBranchName().trim();

        if (branchRepository
                .findByBranchName(branchName)
                .isPresent()) {

            throw new IllegalArgumentException(
                    "Branch with this name already exists"
            );
        }

        Branch branch = new Branch();

        branch.setBranchName(branchName);
        branch.setAddress(
                request.getAddress()
        );

        Branch savedBranch =
                branchRepository.save(branch);

        return mapToResponse(savedBranch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminBranchResponse> getAllBranches() {

        return branchRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminBranchResponse getBranchById(
            Long branchId) {

        Branch branch =
                branchRepository.findById(branchId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Branch not found with id: "
                                                + branchId
                                )
                        );

        return mapToResponse(branch);
    }

    @Override
    @Transactional
    public void deleteBranch(Long branchId) {

        Branch branch =
                branchRepository.findById(branchId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Branch not found with id: "
                                                + branchId
                                )
                        );

        branchRepository.delete(branch);
    }

    private AdminBranchResponse mapToResponse(
            Branch branch) {

        return new AdminBranchResponse(
                branch.getId(),
                branch.getBranchName(),
                branch.getAddress(),
                branch.getActive()
        );
    }
}
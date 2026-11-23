package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.admin.AdminBranchRequest;
import com.autism_rehablito_app.dto.admin.AdminBranchResponse;

import java.util.List;

public interface AdminBranchService {

    AdminBranchResponse addBranch(
            AdminBranchRequest request
    );

    List<AdminBranchResponse> getAllBranches();

    AdminBranchResponse getBranchById(
            Long branchId
    );

    void deleteBranch(
            Long branchId
    );
}
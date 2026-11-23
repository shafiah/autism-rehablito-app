package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminBranchRequest;
import com.autism_rehablito_app.dto.admin.AdminBranchResponse;
import com.autism_rehablito_app.service.AdminBranchService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/branches")
public class AdminBranchController {

    private final AdminBranchService adminBranchService;

    public AdminBranchController(
            AdminBranchService adminBranchService) {

        this.adminBranchService = adminBranchService;
    }

    @PostMapping
    public ResponseEntity<AdminBranchResponse> addBranch(
            @Valid @RequestBody AdminBranchRequest request) {

        AdminBranchResponse response =
                adminBranchService.addBranch(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AdminBranchResponse>>
    getAllBranches() {

        return ResponseEntity.ok(
                adminBranchService.getAllBranches()
        );
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<AdminBranchResponse>
    getBranchById(
            @PathVariable Long branchId) {

        return ResponseEntity.ok(
                adminBranchService.getBranchById(
                        branchId
                )
        );
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> deleteBranch(
            @PathVariable Long branchId) {

        adminBranchService.deleteBranch(
                branchId
        );

        return ResponseEntity.noContent().build();
    }
}
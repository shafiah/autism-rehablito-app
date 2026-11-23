
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminParentOptionResponse;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ParentRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/parents")
public class AdminParentController {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;

    public AdminParentController(
            ParentRepository parentRepository,
            ChildRepository childRepository) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
    }

    @GetMapping
    public ResponseEntity<List<AdminParentOptionResponse>>
    getActiveParents() {

        List<AdminParentOptionResponse> parents =
                parentRepository.findAll()
                        .stream()
                        .filter(parent ->
                                Boolean.TRUE.equals(parent.getActive()))
                        .map(this::mapToResponse)
                        .toList();

        return ResponseEntity.ok(parents);
    }

    private AdminParentOptionResponse mapToResponse(
            Parent parent) {

        Long branchId = null;
        String branchName = null;

        if (parent.getBranch() != null) {
            branchId = parent.getBranch().getId();
            branchName = parent.getBranch().getBranchName();
        }

        boolean childLinked =
                childRepository.existsByParentIdAndActiveTrue(
                        parent.getId()
                );

        return new AdminParentOptionResponse(
                parent.getId(),
                parent.getUser().getName(),
                parent.getUser().getEmail(),
                parent.getUser().getMobileNumber(),
                parent.getActive(),
                branchId,
                branchName,
                childLinked
        );
    }
}

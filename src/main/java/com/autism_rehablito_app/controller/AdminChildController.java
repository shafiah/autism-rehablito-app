package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.admin.AdminChildCreateRequest;
import com.autism_rehablito_app.dto.admin.AdminChildResponse;
import com.autism_rehablito_app.dto.admin.AdminChildUpdateRequest;
import com.autism_rehablito_app.dto.admin.AdminParentOptionResponse;
import com.autism_rehablito_app.service.AdminChildService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaTypeFactory;

import java.util.List;

@RestController
@RequestMapping("/api/admin/children")
public class AdminChildController {

    private final AdminChildService adminChildService;

    public AdminChildController(
            AdminChildService adminChildService) {

        this.adminChildService = adminChildService;
    }

    @PostMapping
    public ResponseEntity<AdminChildResponse> createChild(
            @Valid @RequestBody AdminChildCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminChildService.createChild(request));
    }

    @GetMapping
    public ResponseEntity<List<AdminChildResponse>> getChildren(
            @RequestParam(required = false) Long branchId) {

        if (branchId != null) {
            return ResponseEntity.ok(
                    adminChildService.getChildrenByBranch(branchId)
            );
        }

        return ResponseEntity.ok(
                adminChildService.getAllChildren()
        );
    }

    @GetMapping("/{childId}")
    public ResponseEntity<AdminChildResponse> getChildById(
            @PathVariable Long childId) {

        return ResponseEntity.ok(
                adminChildService.getChildById(childId)
        );
    }
    
    @PostMapping(
            value = "/{childId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AdminChildResponse> uploadChildImage(
            @PathVariable Long childId,
            @RequestParam("image") MultipartFile image) {

        return ResponseEntity.ok(
                adminChildService.uploadChildImage(childId, image)
        );
    }
    
    @PutMapping("/{childId}")
    public ResponseEntity<AdminChildResponse> updateChild(
            @PathVariable Long childId,
            @Valid @RequestBody AdminChildUpdateRequest request) {

        return ResponseEntity.ok(
                adminChildService.updateChild(childId, request)
        );
    }
    
    @PutMapping("/{childId}/activate")
    public ResponseEntity<AdminChildResponse> activateChild(
            @PathVariable Long childId) {

        return ResponseEntity.ok(
                adminChildService.activateChild(childId)
        );
    }
    
    @PutMapping("/{childId}/deactivate")
    public ResponseEntity<AdminChildResponse> deactivateChild(
            @PathVariable Long childId) {

        return ResponseEntity.ok(
                adminChildService.deactivateChild(childId)
        );
    }
    
    @GetMapping("/parents")
    public ResponseEntity<List<AdminParentOptionResponse>> getParentsByBranch(
            @RequestParam Long branchId) {

        return ResponseEntity.ok(
                adminChildService.getParentsByBranch(branchId)
        );
    }
    
    @GetMapping("/{childId}/image")
    public ResponseEntity<Resource> getChildImage(
            @PathVariable Long childId) {

        Resource image = adminChildService.getChildImage(childId);

        MediaType mediaType = MediaTypeFactory
                .getMediaType(image)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(image);
    }
}
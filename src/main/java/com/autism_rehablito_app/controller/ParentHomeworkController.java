package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.parent.ParentHomeworkRemarkRequest;
import com.autism_rehablito_app.dto.parent.ParentHomeworkResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.ParentHomeworkService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent/children/{childId}/homework")
public class ParentHomeworkController {

    private final ParentHomeworkService parentHomeworkService;

    public ParentHomeworkController(
            ParentHomeworkService parentHomeworkService) {

        this.parentHomeworkService = parentHomeworkService;
    }

    @GetMapping
    public ResponseEntity<List<ParentHomeworkResponse>> getMyChildHomework(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentHomeworkService.getMyChildHomework(
                        user.getId(),
                        childId
                )
        );
    }

    @GetMapping("/{homeworkId}")
    public ResponseEntity<ParentHomeworkResponse> getMyChildHomeworkById(
            Authentication authentication,
            @PathVariable Long childId,
            @PathVariable Long homeworkId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentHomeworkService.getMyChildHomeworkById(
                        user.getId(),
                        childId,
                        homeworkId
                )
        );
    }
    
    @PutMapping("/{homeworkId}/remark")
    public ResponseEntity<ParentHomeworkResponse> addOrUpdateRemark(
            Authentication authentication,
            @PathVariable Long childId,
            @PathVariable Long homeworkId,
            @Valid @RequestBody ParentHomeworkRemarkRequest request) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                parentHomeworkService.addOrUpdateRemark(
                        user.getId(),
                        childId,
                        homeworkId,
                        request
                )
        );
    }
}
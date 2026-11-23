package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.therapist.TherapistHomeworkCreateRequest;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkRemarksRequest;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkResponse;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkStatusUpdateRequest;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkUpdateRequest;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.TherapistHomeworkService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/therapist/homework")
public class TherapistHomeworkController {

    private final TherapistHomeworkService therapistHomeworkService;

    public TherapistHomeworkController(
            TherapistHomeworkService therapistHomeworkService) {

        this.therapistHomeworkService =
                therapistHomeworkService;
    }

    @PostMapping("/children/{childId}")
    public ResponseEntity<TherapistHomeworkResponse> createHomework(
            Authentication authentication,
            @PathVariable Long childId,
            @Valid @RequestBody TherapistHomeworkCreateRequest request) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        therapistHomeworkService.createHomework(
                                user.getId(),
                                childId,
                                request
                        )
                );
    }
    
    @GetMapping
    public ResponseEntity<List<TherapistHomeworkResponse>> getMyHomework(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistHomeworkService.getMyHomework(
                        user.getId()
                )
        );
    }
    
    @GetMapping("/{homeworkId}")
    public ResponseEntity<TherapistHomeworkResponse> getMyHomeworkById(
            Authentication authentication,
            @PathVariable Long homeworkId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistHomeworkService.getMyHomeworkById(
                        user.getId(),
                        homeworkId
                )
        );
    }
    
    @PutMapping("/{homeworkId}/status")
    public ResponseEntity<TherapistHomeworkResponse> updateHomeworkStatus(
            Authentication authentication,
            @PathVariable Long homeworkId,
            @Valid @RequestBody TherapistHomeworkStatusUpdateRequest request) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistHomeworkService.updateHomeworkStatus(
                        user.getId(),
                        homeworkId,
                        request
                )
        );
    }
    
 // Update existing homework
    @PutMapping("/{homeworkId}")
    public ResponseEntity<TherapistHomeworkResponse> updateHomework(
            Authentication authentication,
            @PathVariable Long homeworkId,
            @Valid @RequestBody TherapistHomeworkUpdateRequest request) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistHomeworkService.updateHomework(
                        user.getId(),
                        homeworkId,
                        request
                )
        );
    }
    
    @PutMapping("/{homeworkId}/remarks")
    public ResponseEntity<TherapistHomeworkResponse> updateTherapistRemarks(
            Authentication authentication,
            @PathVariable Long homeworkId,
            @Valid @RequestBody TherapistHomeworkRemarksRequest request) {

        User user = (User) authentication.getPrincipal();

        TherapistHomeworkResponse response =
                therapistHomeworkService.updateTherapistRemarks(
                        user.getId(),
                        homeworkId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // Delete existing homework
    @DeleteMapping("/{homeworkId}")
    public ResponseEntity<Void> deleteHomework(
            Authentication authentication,
            @PathVariable Long homeworkId) {

        User user = (User) authentication.getPrincipal();

        therapistHomeworkService.deleteHomework(
                user.getId(),
                homeworkId
        );

        return ResponseEntity.noContent().build();
    }
}
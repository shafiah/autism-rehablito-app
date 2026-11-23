package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.therapist.TherapistChildResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.TherapistChildService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapist/children")
public class TherapistChildController {

    private final TherapistChildService therapistChildService;

    public TherapistChildController(
            TherapistChildService therapistChildService) {

        this.therapistChildService =
                therapistChildService;
    }

    @GetMapping
    public ResponseEntity<List<TherapistChildResponse>>
    getMyChildren(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistChildService.getMyChildren(
                        user.getId()
                )
        );
    }

    @GetMapping("/{childId}")
    public ResponseEntity<TherapistChildResponse>
    getMyChildById(
            Authentication authentication,
            @PathVariable Long childId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                therapistChildService.getMyChildById(
                        user.getId(),
                        childId
                )
        );
    }
}
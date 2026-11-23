package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.enquiry.EnquiryRequest;
import com.autism_rehablito_app.dto.enquiry.EnquiryResponse;
import com.autism_rehablito_app.dto.enquiry.EnquiryStatusUpdateRequest;
import com.autism_rehablito_app.service.EnquiryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    // Submit new enquiry
    @PostMapping
    public ResponseEntity<EnquiryResponse> submitEnquiry(
            @Valid @RequestBody EnquiryRequest request) {

        EnquiryResponse response = enquiryService.submitEnquiry(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get all enquiries
    @GetMapping
    public ResponseEntity<List<EnquiryResponse>> getAllEnquiries() {

        List<EnquiryResponse> enquiries = enquiryService.getAllEnquiries();

        return ResponseEntity.ok(enquiries);
    }

    // Get enquiry by ID
    @GetMapping("/{enquiryId}")
    public ResponseEntity<EnquiryResponse> getEnquiryById(
            @PathVariable Long enquiryId) {

        EnquiryResponse response = enquiryService.getEnquiryById(enquiryId);

        return ResponseEntity.ok(response);
    }
    
 // Update enquiry status
    @PutMapping("/{enquiryId}/status")
    public ResponseEntity<EnquiryResponse> updateEnquiryStatus(
            @PathVariable Long enquiryId,
            @Valid @RequestBody EnquiryStatusUpdateRequest request) {

        EnquiryResponse response = enquiryService.updateEnquiryStatus(
                enquiryId,
                request.getStatus()
        );

        return ResponseEntity.ok(response);
    }
}
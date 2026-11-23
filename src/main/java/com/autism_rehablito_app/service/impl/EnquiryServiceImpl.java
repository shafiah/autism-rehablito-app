package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.enquiry.EnquiryRequest;
import com.autism_rehablito_app.dto.enquiry.EnquiryResponse;
import com.autism_rehablito_app.entity.Enquiry;
import com.autism_rehablito_app.repository.EnquiryRepository;
import com.autism_rehablito_app.service.EnquiryService;
import com.autism_rehablito_app.util.EnquiryStatusValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    // Submit new enquiry
    @Override
    @Transactional
    public EnquiryResponse submitEnquiry(EnquiryRequest request) {

        Enquiry enquiry = new Enquiry();

        enquiry.setParentName(request.getParentName().trim());
        enquiry.setMobileNumber(request.getMobileNumber().trim());
        enquiry.setChildAge(request.getChildAge());
        enquiry.setEnquiryPurpose(request.getEnquiryPurpose());

        Enquiry savedEnquiry = enquiryRepository.save(enquiry);

        return mapToResponse(savedEnquiry);
    }

    // Get all enquiries
    @Override
    @Transactional(readOnly = true)
    public List<EnquiryResponse> getAllEnquiries() {

        return enquiryRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get enquiry by ID
    @Override
    @Transactional(readOnly = true)
    public EnquiryResponse getEnquiryById(Long enquiryId) {

        Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Enquiry not found with id: " + enquiryId));

        return mapToResponse(enquiry);
    }

    // Entity to DTO mapping
    private EnquiryResponse mapToResponse(Enquiry enquiry) {

        return new EnquiryResponse(
                enquiry.getId(),
                enquiry.getParentName(),
                enquiry.getMobileNumber(),
                enquiry.getChildAge(),
                enquiry.getEnquiryPurpose(),
                enquiry.getStatus(),
                enquiry.getCreatedAt(),
                enquiry.getUpdatedAt()
        );
    }
    
    @Override
    @Transactional
    public EnquiryResponse updateEnquiryStatus(Long enquiryId, String status) {

        if (!EnquiryStatusValidator.isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid enquiry status: " + status);
        }

        Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Enquiry not found with id: " + enquiryId));

        enquiry.setStatus(status.trim().toUpperCase());

        Enquiry updatedEnquiry = enquiryRepository.saveAndFlush(enquiry);
        return mapToResponse(updatedEnquiry);
    }
}
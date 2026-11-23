package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.enquiry.EnquiryRequest;
import com.autism_rehablito_app.dto.enquiry.EnquiryResponse;

import java.util.List;

public interface EnquiryService {

    EnquiryResponse submitEnquiry(
            EnquiryRequest request
    );

    List<EnquiryResponse> getAllEnquiries();

    EnquiryResponse getEnquiryById(
            Long enquiryId
    );
    
    EnquiryResponse updateEnquiryStatus(Long enquiryId, String status);
}
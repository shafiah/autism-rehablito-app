package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.ParentCreatePaymentRequest;
import com.autism_rehablito_app.dto.ParentCreatePaymentResponse;
import com.autism_rehablito_app.dto.ParentPaymentDetailsResponse;
import com.autism_rehablito_app.dto.ParentPaymentOptionResponse;
import com.autism_rehablito_app.dto.ParentPaymentSummaryResponse;
import com.autism_rehablito_app.dto.ParentVerifyPaymentRequest;
import com.autism_rehablito_app.dto.ParentVerifyPaymentResponse;

import java.util.List;

public interface ParentPaymentService {

    List<ParentPaymentOptionResponse> getPaymentOptions(
            Long userId,
            Long childId
    );

    ParentPaymentSummaryResponse getPaymentSummary(
            Long userId,
            Long childId,
            String therapyType
    );

    ParentCreatePaymentResponse createPaymentOrder(
            Long userId,
            ParentCreatePaymentRequest request
    );
    
    ParentVerifyPaymentResponse verifyPayment(
            Long userId,
            ParentVerifyPaymentRequest request
    );
    
    ParentPaymentDetailsResponse getPaymentDetails(Long userId, Long paymentId);
    
    List<ParentPaymentDetailsResponse> getPaymentHistory(Long userId);
}
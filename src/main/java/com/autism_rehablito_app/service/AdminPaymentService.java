
package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.AdminPaymentResponseDto;
import com.autism_rehablito_app.dto.AdminPaymentSummaryResponseDto;

import java.util.List;

public interface AdminPaymentService {

    // All branches - successful payments
    List<AdminPaymentResponseDto> getAllPaidPayments();

    // All branches - all non-successful payments
    List<AdminPaymentResponseDto> getAllUnpaidPayments();

    // Branch-wise successful payments
    List<AdminPaymentResponseDto> getPaidPaymentsByBranch(Long branchId);

    // Branch-wise non-successful payments
    List<AdminPaymentResponseDto> getUnpaidPaymentsByBranch(Long branchId);
    

/*
 * Admin payment summary across all branches.
 */
AdminPaymentSummaryResponseDto getAllPaymentSummary();

/*
 * Admin payment summary for a particular branch.
 */
AdminPaymentSummaryResponseDto getPaymentSummaryByBranch(Long branchId);

}

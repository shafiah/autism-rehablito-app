
package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.AdminPaymentResponseDto;
import com.autism_rehablito_app.service.AdminPaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.autism_rehablito_app.dto.AdminPaymentSummaryResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    public AdminPaymentController(AdminPaymentService adminPaymentService) {
        this.adminPaymentService = adminPaymentService;
    }

    // Get all successful payments across all branches
    @GetMapping("/paid")
    public ResponseEntity<List<AdminPaymentResponseDto>> getAllPaidPayments() {

        List<AdminPaymentResponseDto> payments =
                adminPaymentService.getAllPaidPayments();

        return ResponseEntity.ok(payments);
    }

    // Get all non-successful payments across all branches
    @GetMapping("/unpaid")
    public ResponseEntity<List<AdminPaymentResponseDto>> getAllUnpaidPayments() {

        List<AdminPaymentResponseDto> payments =
                adminPaymentService.getAllUnpaidPayments();

        return ResponseEntity.ok(payments);
    }

    // Get successful payments for a particular branch
    @GetMapping("/branch/{branchId}/paid")
    public ResponseEntity<List<AdminPaymentResponseDto>> getPaidPaymentsByBranch(
            @PathVariable Long branchId) {

        List<AdminPaymentResponseDto> payments =
                adminPaymentService.getPaidPaymentsByBranch(branchId);

        return ResponseEntity.ok(payments);
    }

    // Get non-successful payments for a particular branch
    @GetMapping("/branch/{branchId}/unpaid")
    public ResponseEntity<List<AdminPaymentResponseDto>> getUnpaidPaymentsByBranch(
            @PathVariable Long branchId) {

        List<AdminPaymentResponseDto> payments =
                adminPaymentService.getUnpaidPaymentsByBranch(branchId);

        return ResponseEntity.ok(payments);
    }
    

 // =====================================================
 // ADMIN PAYMENT SUMMARY
 // =====================================================

 // Get payment summary across all branches
 @GetMapping("/summary")
 public ResponseEntity<AdminPaymentSummaryResponseDto> getAllPaymentSummary() {

     AdminPaymentSummaryResponseDto summary =
             adminPaymentService.getAllPaymentSummary();

     return ResponseEntity.ok(summary);
 }

 // Get payment summary for a particular branch
 @GetMapping("/branch/{branchId}/summary")
 public ResponseEntity<AdminPaymentSummaryResponseDto> getPaymentSummaryByBranch(
         @PathVariable Long branchId) {

     AdminPaymentSummaryResponseDto summary =
             adminPaymentService.getPaymentSummaryByBranch(branchId);

     return ResponseEntity.ok(summary);
 }

}

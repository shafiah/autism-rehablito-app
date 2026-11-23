package com.autism_rehablito_app.controller;

import com.autism_rehablito_app.dto.ParentCreatePaymentRequest;
import com.autism_rehablito_app.dto.ParentCreatePaymentResponse;
import com.autism_rehablito_app.dto.ParentPaymentDetailsResponse;
import com.autism_rehablito_app.dto.ParentPaymentOptionResponse;
import com.autism_rehablito_app.dto.ParentPaymentSummaryResponse;
import com.autism_rehablito_app.dto.ParentVerifyPaymentRequest;
import com.autism_rehablito_app.dto.ParentVerifyPaymentResponse;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.service.ParentPaymentReceiptService;
import com.autism_rehablito_app.service.ParentPaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent/payments")
public class ParentPaymentController {

    private final ParentPaymentService parentPaymentService;
    private final ParentPaymentReceiptService parentPaymentReceiptService;

    public ParentPaymentController(
            ParentPaymentService parentPaymentService,
            ParentPaymentReceiptService parentPaymentReceiptService
    ) {
        this.parentPaymentService = parentPaymentService;
        this.parentPaymentReceiptService = parentPaymentReceiptService;
    }

    /*
     * Get available payment therapy options.
     */
    @GetMapping("/children/{childId}/options")
    public ResponseEntity<List<ParentPaymentOptionResponse>>
    getPaymentOptions(
            @PathVariable Long childId,
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                parentPaymentService.getPaymentOptions(
                        userId,
                        childId
                )
        );
    }

    /*
     * Get payment summary before Razorpay checkout.
     */
    @GetMapping("/children/{childId}/summary")
    public ResponseEntity<ParentPaymentSummaryResponse>
    getPaymentSummary(
            @PathVariable Long childId,
            @RequestParam String therapyType,
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                parentPaymentService.getPaymentSummary(
                        userId,
                        childId,
                        therapyType
                )
        );
    }

    /*
     * Create Razorpay order.
     */
    @PostMapping("/create-order")
    public ResponseEntity<ParentCreatePaymentResponse>
    createPaymentOrder(
            @Valid @RequestBody ParentCreatePaymentRequest request,
            Authentication authentication
    ) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                parentPaymentService.createPaymentOrder(
                        userId,
                        request
                )
        );
    }

    /*
     * Get authenticated user's ID.
     *
     * Our existing JWT authentication filter stores
     * the actual application User entity as principal.
     */
    private Long getUserId(
            Authentication authentication
    ) {

        if (authentication == null ||
                authentication.getPrincipal() == null) {

            throw new IllegalStateException(
                    "Authenticated user information not available"
            );
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return user.getId();
        }

        throw new IllegalStateException(
                "Authenticated user information not available"
        );
    }
    
    @PostMapping("/verify")
    public ResponseEntity<ParentVerifyPaymentResponse> verifyPayment(
            @Valid @RequestBody ParentVerifyPaymentRequest request,
            Authentication authentication) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                parentPaymentService.verifyPayment(
                        userId,
                        request
                )
        );
    }
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<ParentPaymentDetailsResponse> getPaymentDetails(
            Authentication authentication,
            @PathVariable Long paymentId) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                parentPaymentService.getPaymentDetails(
                        userId,
                        paymentId
                )
        );
    }
    
    @GetMapping("/history")
    public ResponseEntity<List<ParentPaymentDetailsResponse>> getPaymentHistory(
            Authentication authentication) {

        Long userId = getUserId(authentication);

        return ResponseEntity.ok(
                parentPaymentService.getPaymentHistory(userId)
        );
    }
    
    @GetMapping("/{paymentId}/receipt")
    public ResponseEntity<byte[]> downloadPaymentReceipt(
            Authentication authentication,
            @PathVariable Long paymentId) {

        Long userId = getUserId(authentication);

        byte[] pdf =
                parentPaymentReceiptService.generateReceipt(
                        userId,
                        paymentId
                );

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=payment-receipt-" +
                                paymentId + ".pdf"
                )
                .header(
                        "Content-Type",
                        "application/pdf"
                )
                .body(pdf);
    }
}
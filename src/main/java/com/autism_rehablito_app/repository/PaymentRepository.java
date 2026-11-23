
package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.Payment;
import com.autism_rehablito_app.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /*
     * Find payment using Razorpay Order ID.
     */
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    /*
     * Find payment using Razorpay Payment ID.
     */
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    /*
     * Find all payments belonging to a parent.
     */
    List<Payment> findByParentIdOrderByCreatedAtDesc(Long parentId);

    /*
     * Find all payments for a particular child.
     */
    List<Payment> findByChildIdOrderByCreatedAtDesc(Long childId);

    /*
     * Find payments for a child with a particular status.
     */
    List<Payment> findByChildIdAndStatusOrderByCreatedAtDesc(
            Long childId,
            PaymentStatus status
    );

    /*
     * Find payments belonging to a parent with a particular status.
     */
    List<Payment> findByParentIdAndStatusOrderByCreatedAtDesc(
            Long parentId,
            PaymentStatus status
    );

    /*
     * Prevent duplicate successful payment processing.
     */
    boolean existsByRazorpayPaymentId(String razorpayPaymentId);

    /*
     * Check whether a Razorpay order already exists.
     */
    boolean existsByRazorpayOrderId(String razorpayOrderId);


    // =====================================================
    // ADMIN PAYMENT MODULE
    // =====================================================

    /*
     * Get all successful payments for Admin.
     */
    List<Payment> findByStatusOrderByCreatedAtDesc(
            PaymentStatus status
    );

    /*
     * Get all non-successful payments for Admin.
     * Includes PENDING, FAILED and ERROR.
     */
    List<Payment> findByStatusNotOrderByCreatedAtDesc(
            PaymentStatus status
    );

    /*
     * Get successful payments branch-wise.
     */
    List<Payment> findByChild_Branch_IdAndStatusOrderByCreatedAtDesc(
            Long branchId,
            PaymentStatus status
    );

    /*
     * Get non-successful payments branch-wise.
     * Includes PENDING, FAILED and ERROR.
     */
    List<Payment> findByChild_Branch_IdAndStatusNotOrderByCreatedAtDesc(
            Long branchId,
            PaymentStatus status
    );
    
 // =====================================================
 // ADMIN PAYMENT SUMMARY
 // =====================================================

 /*
  * Get all successful payments for summary calculation.
  */
 List<Payment> findByStatus(PaymentStatus status);

 /*
  * Get all non-successful payments for summary calculation.
  */
 List<Payment> findByStatusNot(PaymentStatus status);
}

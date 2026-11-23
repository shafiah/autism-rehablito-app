
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.AdminPaymentResponseDto;
import com.autism_rehablito_app.entity.Payment;
import com.autism_rehablito_app.entity.PaymentStatus;
import com.autism_rehablito_app.repository.PaymentRepository;
import com.autism_rehablito_app.service.AdminPaymentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.autism_rehablito_app.dto.AdminPaymentSummaryResponseDto;
import java.math.BigDecimal;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final PaymentRepository paymentRepository;

    public AdminPaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<AdminPaymentResponseDto> getAllPaidPayments() {

        List<Payment> payments =
                paymentRepository.findByStatusOrderByCreatedAtDesc(
                        PaymentStatus.SUCCESS
                );

        return mapToDto(payments);
    }

    @Override
    public List<AdminPaymentResponseDto> getAllUnpaidPayments() {

        List<Payment> payments =
                paymentRepository.findByStatusNotOrderByCreatedAtDesc(
                        PaymentStatus.SUCCESS
                );

        return mapToDto(payments);
    }

    @Override
    public List<AdminPaymentResponseDto> getPaidPaymentsByBranch(Long branchId) {

        List<Payment> payments =
                paymentRepository.findByChild_Branch_IdAndStatusOrderByCreatedAtDesc(
                        branchId,
                        PaymentStatus.SUCCESS
                );

        return mapToDto(payments);
    }

    @Override
    public List<AdminPaymentResponseDto> getUnpaidPaymentsByBranch(Long branchId) {

        List<Payment> payments =
                paymentRepository.findByChild_Branch_IdAndStatusNotOrderByCreatedAtDesc(
                        branchId,
                        PaymentStatus.SUCCESS
                );

        return mapToDto(payments);
    }

    private List<AdminPaymentResponseDto> mapToDto(List<Payment> payments) {

        return payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AdminPaymentResponseDto convertToDto(Payment payment) {

        AdminPaymentResponseDto dto = new AdminPaymentResponseDto();

        dto.setPaymentId(payment.getId());

        dto.setChildId(payment.getChild().getId());
        dto.setChildName(payment.getChild().getChildName());

        dto.setParentId(payment.getParent().getId());
        dto.setParentName(
                payment.getParent().getUser().getName()
        );

        dto.setBranchId(payment.getChild().getBranch().getId());
        dto.setBranchName(
                payment.getChild().getBranch().getBranchName()
        );

        dto.setTherapyType(payment.getTherapyType());

        dto.setActualFee(payment.getActualFee());
        dto.setDiscount(payment.getDiscount());
        dto.setFinalPayable(payment.getFinalPayable());
        dto.setPaidAmount(payment.getPaidAmount());

        dto.setCurrency(payment.getCurrency());
        dto.setStatus(payment.getStatus());

        dto.setReceiptReference(payment.getReceiptReference());

        dto.setPaymentDate(payment.getPaymentDate());
        dto.setCreatedAt(payment.getCreatedAt());

        return dto;
    }
    

@Override
public AdminPaymentSummaryResponseDto getAllPaymentSummary() {

    List<Payment> successfulPayments =
            paymentRepository.findByStatus(PaymentStatus.SUCCESS);

    List<Payment> nonSuccessfulPayments =
            paymentRepository.findByStatusNot(PaymentStatus.SUCCESS);

    return calculateSummary(successfulPayments, nonSuccessfulPayments);
}

@Override
public AdminPaymentSummaryResponseDto getPaymentSummaryByBranch(Long branchId) {

    List<Payment> successfulPayments =
            paymentRepository.findByChild_Branch_IdAndStatusOrderByCreatedAtDesc(
                    branchId,
                    PaymentStatus.SUCCESS
            );

    List<Payment> nonSuccessfulPayments =
            paymentRepository.findByChild_Branch_IdAndStatusNotOrderByCreatedAtDesc(
                    branchId,
                    PaymentStatus.SUCCESS
            );

    return calculateSummary(successfulPayments, nonSuccessfulPayments);
}

/*
 * Calculate Admin Payment Summary.
 */
private AdminPaymentSummaryResponseDto calculateSummary(
        List<Payment> successfulPayments,
        List<Payment> nonSuccessfulPayments) {

    AdminPaymentSummaryResponseDto summary =
            new AdminPaymentSummaryResponseDto();

    // Total Collection: Only successful payments
    BigDecimal totalCollection = successfulPayments.stream()
            .map(payment -> payment.getPaidAmount() != null
                    ? payment.getPaidAmount()
                    : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Non-successful transaction amount
    BigDecimal pendingTransactionAmount = nonSuccessfulPayments.stream()
            .map(payment -> payment.getFinalPayable() != null
                    ? payment.getFinalPayable()
                    : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Pending transaction count
    long pendingCount = nonSuccessfulPayments.stream()
            .filter(payment -> payment.getStatus() == PaymentStatus.PENDING)
            .count();

    // Failed + Error transaction count
    long failedCount = nonSuccessfulPayments.stream()
            .filter(payment ->
                    payment.getStatus() == PaymentStatus.FAILED
                    || payment.getStatus() == PaymentStatus.ERROR)
            .count();

    summary.setTotalCollection(totalCollection);

    summary.setPendingTransactionAmount(pendingTransactionAmount);

    summary.setSuccessfulTransactions((long) successfulPayments.size());

    summary.setPendingTransactions(pendingCount);

    summary.setFailedTransactions(failedCount);

    summary.setTotalTransactions(
            (long) successfulPayments.size() + nonSuccessfulPayments.size()
    );

    return summary;
}

}

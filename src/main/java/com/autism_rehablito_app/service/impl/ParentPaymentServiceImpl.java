package com.autism_rehablito_app.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.ZoneId;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autism_rehablito_app.dto.ParentCreatePaymentRequest;
import com.autism_rehablito_app.dto.ParentCreatePaymentResponse;
import com.autism_rehablito_app.dto.ParentPaymentDetailsResponse;
import com.autism_rehablito_app.dto.ParentPaymentOptionResponse;
import com.autism_rehablito_app.dto.ParentPaymentSummaryResponse;
import com.autism_rehablito_app.dto.ParentVerifyPaymentRequest;
import com.autism_rehablito_app.dto.ParentVerifyPaymentResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.Payment;
import com.autism_rehablito_app.entity.PaymentStatus;
import com.autism_rehablito_app.entity.TherapyPricing;
import com.autism_rehablito_app.exception.ResourceNotFoundException;
import com.autism_rehablito_app.exception.UnauthorizedException;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.repository.PaymentRepository;
import com.autism_rehablito_app.repository.TherapyPricingRepository;
import com.autism_rehablito_app.service.ParentPaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import com.autism_rehablito_app.entity.NotificationType;
import com.autism_rehablito_app.service.NotificationEventService;

@Service
@Transactional
public class ParentPaymentServiceImpl implements ParentPaymentService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final TherapyPricingRepository therapyPricingRepository;
    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;
    private final NotificationEventService notificationEventService;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    public ParentPaymentServiceImpl(
            ParentRepository parentRepository,
            ChildRepository childRepository,
            TherapyPricingRepository therapyPricingRepository,
            PaymentRepository paymentRepository,
            RazorpayClient razorpayClient,
            NotificationEventService notificationEventService
    ) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.therapyPricingRepository = therapyPricingRepository;
        this.paymentRepository = paymentRepository;
        this.razorpayClient = razorpayClient;
        this.notificationEventService = notificationEventService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParentPaymentOptionResponse> getPaymentOptions(
            Long userId,
            Long childId
    ) {

        Parent parent = getActiveParent(userId);

        Child child = getChildForParent(parent, childId);

        if (!child.getActive()) {
            throw new UnauthorizedException(
                    "Child is not active"
            );
        }

        return therapyPricingRepository
                .findByActiveTrueOrderByTherapyTypeAsc()
                .stream()
                .map(pricing -> new ParentPaymentOptionResponse(
                        pricing.getTherapyType(),
                        pricing.getFee(),
                        "INR"
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ParentPaymentSummaryResponse getPaymentSummary(
            Long userId,
            Long childId,
            String therapyType
    ) {

        Parent parent = getActiveParent(userId);

        Child child = getChildForParent(parent, childId);

        if (!child.getActive()) {
            throw new UnauthorizedException(
                    "Child is not active"
            );
        }

        TherapyPricing pricing = getActiveTherapyPricing(therapyType);

        BigDecimal actualFee = pricing.getFee();

        BigDecimal discount = BigDecimal.ZERO;

        BigDecimal finalPayable = actualFee
                .subtract(discount)
                .setScale(2, RoundingMode.HALF_UP);

        return new ParentPaymentSummaryResponse(
                child.getId(),
                child.getChildName(),
                pricing.getTherapyType(),
                actualFee,
                discount,
                finalPayable,
                "INR"
        );
    }

    @Override
    public ParentCreatePaymentResponse createPaymentOrder(
            Long userId,
            ParentCreatePaymentRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Payment request is required"
            );
        }

        Parent parent = getActiveParent(userId);

        Child child = getChildForParent(
                parent,
                request.getChildId()
        );

        if (!child.getActive()) {
            throw new UnauthorizedException(
                    "Child is not active"
            );
        }

        TherapyPricing pricing = getActiveTherapyPricing(
                request.getTherapyType()
        );

        BigDecimal actualFee = pricing.getFee();

        if (actualFee == null ||
                actualFee.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Invalid therapy fee"
            );
        }

        /*
         * Parent cannot provide a discount.
         *
         * Discount will always be ZERO for Razorpay
         * parent payments.
         */
        BigDecimal discount = BigDecimal.ZERO;

        BigDecimal finalPayable = actualFee
                .subtract(discount)
                .setScale(2, RoundingMode.HALF_UP);

        if (finalPayable.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Invalid final payable amount"
            );
        }

        /*
         * Razorpay requires amount in paise.
         *
         * Example:
         * ₹5000.00 = 500000 paise
         */
        long amountInPaise = finalPayable
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        String receiptReference =
                generateReceiptReference();

        try {

            JSONObject orderRequest = new JSONObject();

            orderRequest.put(
                    "amount",
                    amountInPaise
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            orderRequest.put(
                    "receipt",
                    receiptReference
            );

            JSONObject notes = new JSONObject();

            notes.put(
                    "childId",
                    child.getId().toString()
            );

            notes.put(
                    "parentId",
                    parent.getId().toString()
            );

            notes.put(
                    "therapyType",
                    pricing.getTherapyType()
            );

            orderRequest.put(
                    "notes",
                    notes
            );

            Order razorpayOrder =
                    razorpayClient.orders.create(orderRequest);

            String razorpayOrderId =
                    razorpayOrder.get("id");

            /*
             * Save our own payment record only after
             * Razorpay successfully creates the order.
             */
            Payment payment = new Payment();

            payment.setParent(parent);
            payment.setChild(child);
            payment.setTherapyType(
                    pricing.getTherapyType()
            );

            payment.setActualFee(actualFee);
            payment.setDiscount(discount);
            payment.setFinalPayable(finalPayable);

            /*
             * No successful payment has happened yet.
             */
            payment.setPaidAmount(BigDecimal.ZERO);

            payment.setCurrency("INR");

            payment.setRazorpayOrderId(
                    razorpayOrderId
            );

            payment.setStatus(
                    PaymentStatus.PENDING
            );

            payment.setReceiptReference(
                    receiptReference
            );

            Payment savedPayment =
                    paymentRepository.save(payment);

            return new ParentCreatePaymentResponse(
                    savedPayment.getId(),
                    child.getId(),
                    child.getChildName(),
                    pricing.getTherapyType(),
                    actualFee,
                    discount,
                    finalPayable,
                    BigDecimal.ZERO,
                    "INR",
                    //razorpayKeySecret,
                    razorpayKeyId,
                    razorpayOrderId,
                    PaymentStatus.PENDING.name(),
                    receiptReference
            );

        } catch (Exception exception) {

            throw new IllegalArgumentException(
                    "Unable to create Razorpay payment order: "
                            + exception.getMessage()
            );
        }
    }

    private Parent getActiveParent(Long userId) {

        Parent parent = parentRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parent profile not found"
                        )
                );

        if (!parent.getActive()) {
            throw new UnauthorizedException(
                    "Parent account is inactive"
            );
        }

        if (!parent.getUser().getActive()) {
            throw new UnauthorizedException(
                    "Parent user account is inactive"
            );
        }

        return parent;
    }

    private Child getChildForParent(
            Parent parent,
            Long childId
    ) {

        if (childId == null) {
            throw new IllegalArgumentException(
                    "Child ID is required"
            );
        }

        Child child = childRepository
                .findById(childId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Child not found"
                        )
                );

        if (!child.getParent()
                .getId()
                .equals(parent.getId())) {

            throw new UnauthorizedException(
                    "You are not authorized to access this child"
            );
        }

        return child;
    }

    private TherapyPricing getActiveTherapyPricing(
            String therapyType
    ) {

        if (therapyType == null ||
                therapyType.isBlank()) {

            throw new IllegalArgumentException(
                    "Therapy type is required"
            );
        }

        TherapyPricing pricing =
                therapyPricingRepository
                        .findByTherapyType(
                                therapyType.trim()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Therapy pricing not found for: "
                                                + therapyType
                                )
                        );

        if (!pricing.getActive()) {
            throw new IllegalArgumentException(
                    "Selected therapy is currently unavailable"
            );
        }

        return pricing;
    }

    private String generateReceiptReference() {

        return "AC-PAY-"
                + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
    
    @Override
    @Transactional
    public ParentVerifyPaymentResponse verifyPayment(
            Long userId,
            ParentVerifyPaymentRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Payment verification request is required"
            );
        }

        Parent parent = getActiveParent(userId);

        Payment payment = paymentRepository
                .findByRazorpayOrderId(
                        request.getRazorpayOrderId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment order not found"
                        )
                );

        /*
         * Parent ownership validation
         */
        if (!payment.getParent()
                .getId()
                .equals(parent.getId())) {

            throw new UnauthorizedException(
                    "You are not authorized to verify this payment"
            );
        }

        /*
         * If already successfully processed,
         * return the existing successful payment.
         */
        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            return buildVerifyPaymentResponse(payment);
        }

        /*
         * Payment ID must not already belong
         * to another payment record.
         */
        if (request.getRazorpayPaymentId() == null ||
                request.getRazorpayPaymentId().isBlank()) {

            throw new IllegalArgumentException(
                    "Razorpay payment ID is required"
            );
        }

        if (paymentRepository.existsByRazorpayPaymentId(
                request.getRazorpayPaymentId())) {

            throw new IllegalArgumentException(
                    "Razorpay payment ID has already been processed"
            );
        }

        try {

            /*
             * --------------------------------------------------
             * STEP 1: Razorpay signature verification
             * --------------------------------------------------
             */

            JSONObject attributes = new JSONObject();

            attributes.put(
                    "razorpay_order_id",
                    request.getRazorpayOrderId()
            );

            attributes.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );

            attributes.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );

            boolean signatureValid =
                    com.razorpay.Utils.verifyPaymentSignature(
                            attributes,
                            razorpayKeySecret
                    );

            if (!signatureValid) {

                throw new IllegalArgumentException(
                        "Razorpay payment signature verification failed"
                );
            }

            /*
             * --------------------------------------------------
             * STEP 2: Fetch actual payment from Razorpay
             * --------------------------------------------------
             */

            com.razorpay.Payment razorpayPayment =
                    razorpayClient.payments.fetch(
                            request.getRazorpayPaymentId()
                    );

            /*
             * --------------------------------------------------
             * STEP 3: Verify Razorpay payment status
             * --------------------------------------------------
             */

            String razorpayStatus =
                    razorpayPayment.get("status");

            if (!"captured".equalsIgnoreCase(
                    razorpayStatus)) {

                throw new IllegalArgumentException(
                        "Razorpay payment is not captured"
                );
            }

            /*
             * --------------------------------------------------
             * STEP 4: Verify Razorpay Order ID
             * --------------------------------------------------
             */

            String razorpayOrderId =
                    razorpayPayment.get("order_id");

            if (razorpayOrderId == null ||
                    !razorpayOrderId.equals(
                            payment.getRazorpayOrderId()
                    )) {

                throw new IllegalArgumentException(
                        "Razorpay order ID mismatch"
                );
            }

            /*
             * --------------------------------------------------
             * STEP 5: Verify amount
             * --------------------------------------------------
             *
             * Razorpay amount is stored in paise.
             */

            Integer razorpayAmount =
                    razorpayPayment.get("amount");

            long expectedAmountInPaise =
                    payment.getFinalPayable()
                            .multiply(BigDecimal.valueOf(100))
                            .longValueExact();

            if (razorpayAmount == null ||
                    razorpayAmount.longValue()
                            != expectedAmountInPaise) {

                throw new IllegalArgumentException(
                        "Razorpay payment amount mismatch"
                );
            }

            /*
             * --------------------------------------------------
             * STEP 6: Verify currency
             * --------------------------------------------------
             */

            String razorpayCurrency =
                    razorpayPayment.get("currency");

            if (razorpayCurrency == null ||
                    !payment.getCurrency()
                            .equalsIgnoreCase(
                                    razorpayCurrency
                            )) {

                throw new IllegalArgumentException(
                        "Razorpay payment currency mismatch"
                );
            }

            /*
             * --------------------------------------------------
             * STEP 7: Persist SUCCESS payment
             * --------------------------------------------------
             */

            payment.setRazorpayPaymentId(
                    request.getRazorpayPaymentId()
            );

            payment.setRazorpaySignature(
                    request.getRazorpaySignature()
            );

            payment.setStatus(
                    PaymentStatus.SUCCESS
            );

            payment.setPaidAmount(
                    payment.getFinalPayable()
            );

//            payment.setPaymentDate(
//                    LocalDateTime.now()
//            );
            payment.setPaymentDate(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));

            Payment savedPayment =
                    paymentRepository.save(payment);

            notificationEventService.notifyUser(
                    parent.getUser().getId(),
                    payment.getChild().getBranch().getId(),
                    "Payment Successful",
                    "Your payment for "
                            + payment.getChild().getChildName()
                            + "'s "
                            + payment.getTherapyType()
                            + " has been completed successfully.",
                    NotificationType.PAYMENT,
                    "PAYMENT",
                    savedPayment.getId()
            );
            
         // Notify all active Admin users about successful payment
            String adminPaymentMessage =
                    "Payment received successfully from "
                            + parent.getUser().getName()
                            + " for child "
                            + savedPayment.getChild().getChildName()
                            + ". Therapy: "
                            + savedPayment.getTherapyType()
                            + ". Amount: "
                            + savedPayment.getCurrency()
                            + " "
                            + savedPayment.getFinalPayable()
                            + ".";

            notificationEventService.notifyAdmins(
                    savedPayment.getChild().getBranch().getId(),
                    "Payment Successful",
                    adminPaymentMessage,
                    NotificationType.PAYMENT,
                    "PAYMENT",
                    savedPayment.getId()
            );

            return buildVerifyPaymentResponse(
                    savedPayment
            );

        } catch (IllegalArgumentException exception) {

            /*
             * Do not mark payment SUCCESS.
             *
             * The original PENDING record is preserved
             * for retry/reconciliation.
             */
            throw exception;

        } catch (Exception exception) {

            /*
             * Razorpay API/network/other verification failure.
             *
             * Keep payment record non-successful.
             */
            throw new IllegalArgumentException(
                    "Unable to verify Razorpay payment"
            );
        }
    }
    
    
    private ParentVerifyPaymentResponse buildVerifyPaymentResponse(
            com.autism_rehablito_app.entity.Payment payment) {

        ParentVerifyPaymentResponse response =
                new ParentVerifyPaymentResponse();

        response.setPaymentId(payment.getId());

        response.setChildId(
                payment.getChild().getId()
        );

        response.setChildName(
                payment.getChild().getChildName()
        );

        response.setTherapyType(
                payment.getTherapyType()
        );

        response.setActualFee(
                payment.getActualFee()
        );

        response.setDiscount(
                payment.getDiscount()
        );

        response.setFinalPayable(
                payment.getFinalPayable()
        );

        response.setPaidAmount(
                payment.getPaidAmount()
        );

        response.setCurrency(
                payment.getCurrency()
        );

        response.setRazorpayOrderId(
                payment.getRazorpayOrderId()
        );

        response.setRazorpayPaymentId(
                payment.getRazorpayPaymentId()
        );

        response.setPaymentStatus(
                payment.getStatus()
        );

        response.setPaymentDate(
                payment.getPaymentDate()
        );

        response.setReceiptReference(
                payment.getReceiptReference()
        );

        return response;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ParentPaymentDetailsResponse getPaymentDetails(
            Long userId,
            Long paymentId) {

        Parent parent = getActiveParent(userId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        if (!payment.getParent().getId().equals(parent.getId())) {
            throw new UnauthorizedException(
                    "You are not authorized to view this payment"
            );
        }

        ParentPaymentDetailsResponse response =
                new ParentPaymentDetailsResponse();

        response.setPaymentId(payment.getId());
        response.setChildName(payment.getChild().getChildName());
        response.setTherapyType(payment.getTherapyType());

        // Amount means the final transaction amount.
        response.setAmount(payment.getFinalPayable());

        response.setStatus(payment.getStatus());
        response.setPaymentDate(payment.getPaymentDate());
        response.setReceiptReference(payment.getReceiptReference());

        return response;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ParentPaymentDetailsResponse> getPaymentHistory(Long userId) {

        Parent parent = getActiveParent(userId);

        List<Payment> payments =
                paymentRepository.findByParentIdOrderByCreatedAtDesc(parent.getId());

        return payments.stream()
                .map(payment -> {

                    ParentPaymentDetailsResponse response =
                            new ParentPaymentDetailsResponse();

                    response.setPaymentId(payment.getId());
                    response.setChildName(payment.getChild().getChildName());
                    response.setTherapyType(payment.getTherapyType());
                    response.setAmount(payment.getFinalPayable());
                    response.setStatus(payment.getStatus());
                    response.setPaymentDate(payment.getPaymentDate());
                    response.setReceiptReference(payment.getReceiptReference());

                    return response;
                })
                .toList();
    }
}
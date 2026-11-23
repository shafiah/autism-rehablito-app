package com.autism_rehablito_app.dto;

import java.math.BigDecimal;

public class ParentCreatePaymentResponse {

    private Long paymentId;

    private Long childId;

    private String childName;

    private String therapyType;

    private BigDecimal actualFee;

    private BigDecimal discount;

    private BigDecimal finalPayable;

    private BigDecimal paidAmount;

    private String currency;

    private String razorpayKeyId;

    private String razorpayOrderId;

    private String paymentStatus;

    private String receiptReference;

    public ParentCreatePaymentResponse() {
    }

    public ParentCreatePaymentResponse(
            Long paymentId,
            Long childId,
            String childName,
            String therapyType,
            BigDecimal actualFee,
            BigDecimal discount,
            BigDecimal finalPayable,
            BigDecimal paidAmount,
            String currency,
            String razorpayKeyId,
            String razorpayOrderId,
            String paymentStatus,
            String receiptReference
    ) {
        this.paymentId = paymentId;
        this.childId = childId;
        this.childName = childName;
        this.therapyType = therapyType;
        this.actualFee = actualFee;
        this.discount = discount;
        this.finalPayable = finalPayable;
        this.paidAmount = paidAmount;
        this.currency = currency;
        this.razorpayKeyId = razorpayKeyId;
        this.razorpayOrderId = razorpayOrderId;
        this.paymentStatus = paymentStatus;
        this.receiptReference = receiptReference;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getTherapyType() {
        return therapyType;
    }

    public void setTherapyType(String therapyType) {
        this.therapyType = therapyType;
    }

    public BigDecimal getActualFee() {
        return actualFee;
    }

    public void setActualFee(BigDecimal actualFee) {
        this.actualFee = actualFee;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFinalPayable() {
        return finalPayable;
    }

    public void setFinalPayable(BigDecimal finalPayable) {
        this.finalPayable = finalPayable;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRazorpayKeyId() {
        return razorpayKeyId;
    }

    public void setRazorpayKeyId(String razorpayKeyId) {
        this.razorpayKeyId = razorpayKeyId;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getReceiptReference() {
        return receiptReference;
    }

    public void setReceiptReference(String receiptReference) {
        this.receiptReference = receiptReference;
    }
}
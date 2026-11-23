package com.autism_rehablito_app.dto;

import java.math.BigDecimal;

public class ParentPaymentSummaryResponse {

    private Long childId;

    private String childName;

    private String therapyType;

    private BigDecimal actualFee;

    private BigDecimal discount;

    private BigDecimal finalPayable;

    private String currency;

    public ParentPaymentSummaryResponse() {
    }

    public ParentPaymentSummaryResponse(
            Long childId,
            String childName,
            String therapyType,
            BigDecimal actualFee,
            BigDecimal discount,
            BigDecimal finalPayable,
            String currency
    ) {
        this.childId = childId;
        this.childName = childName;
        this.therapyType = therapyType;
        this.actualFee = actualFee;
        this.discount = discount;
        this.finalPayable = finalPayable;
        this.currency = currency;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
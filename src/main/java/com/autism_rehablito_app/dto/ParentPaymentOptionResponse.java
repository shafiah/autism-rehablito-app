package com.autism_rehablito_app.dto;

import java.math.BigDecimal;

public class ParentPaymentOptionResponse {

    private String therapyType;

    private BigDecimal actualFee;

    private String currency;

    public ParentPaymentOptionResponse() {
    }

    public ParentPaymentOptionResponse(
            String therapyType,
            BigDecimal actualFee,
            String currency
    ) {
        this.therapyType = therapyType;
        this.actualFee = actualFee;
        this.currency = currency;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
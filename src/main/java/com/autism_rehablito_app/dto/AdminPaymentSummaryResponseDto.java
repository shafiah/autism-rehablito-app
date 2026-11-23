
package com.autism_rehablito_app.dto;

import java.math.BigDecimal;

public class AdminPaymentSummaryResponseDto {

    private BigDecimal totalCollection = BigDecimal.ZERO;
    private BigDecimal pendingTransactionAmount = BigDecimal.ZERO;

    private Long totalTransactions = 0L;
    private Long successfulTransactions = 0L;
    private Long pendingTransactions = 0L;
    private Long failedTransactions = 0L;

    public AdminPaymentSummaryResponseDto() {
    }

    public BigDecimal getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(BigDecimal totalCollection) {
        this.totalCollection = totalCollection;
    }

    public BigDecimal getPendingTransactionAmount() {
        return pendingTransactionAmount;
    }

    public void setPendingTransactionAmount(BigDecimal pendingTransactionAmount) {
        this.pendingTransactionAmount = pendingTransactionAmount;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Long getSuccessfulTransactions() {
        return successfulTransactions;
    }

    public void setSuccessfulTransactions(Long successfulTransactions) {
        this.successfulTransactions = successfulTransactions;
    }

    public Long getPendingTransactions() {
        return pendingTransactions;
    }

    public void setPendingTransactions(Long pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public Long getFailedTransactions() {
        return failedTransactions;
    }

    public void setFailedTransactions(Long failedTransactions) {
        this.failedTransactions = failedTransactions;
    }
}

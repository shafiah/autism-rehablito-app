package com.autism_rehablito_app.service;

public interface ParentPaymentReceiptService {

    byte[] generateReceipt(Long userId, Long paymentId);
}
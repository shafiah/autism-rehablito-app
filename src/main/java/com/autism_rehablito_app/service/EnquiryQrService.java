package com.autism_rehablito_app.service;

public interface EnquiryQrService {

    boolean isEnquiryQrEnabled();

    String getPublicFormUrl();

    void validateEnquiryQrEnabled();
    
    byte[] generateEnquiryQrCode(int width, int height);
}
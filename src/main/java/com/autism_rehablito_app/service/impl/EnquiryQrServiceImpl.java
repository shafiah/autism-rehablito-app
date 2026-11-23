package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.config.EnquiryQrFeatureGuard;
import com.autism_rehablito_app.config.EnquiryQrProperties;
import com.autism_rehablito_app.service.EnquiryQrService;
import com.autism_rehablito_app.util.EnquiryQrGenerator;

import org.springframework.stereotype.Service;

@Service
public class EnquiryQrServiceImpl implements EnquiryQrService {

    private final EnquiryQrFeatureGuard enquiryQrFeatureGuard;
    private final EnquiryQrProperties enquiryQrProperties;
    private final EnquiryQrGenerator enquiryQrGenerator;

    public EnquiryQrServiceImpl(
            EnquiryQrFeatureGuard enquiryQrFeatureGuard,
            EnquiryQrProperties enquiryQrProperties,
            EnquiryQrGenerator enquiryQrGenerator) {

        this.enquiryQrFeatureGuard = enquiryQrFeatureGuard;
        this.enquiryQrProperties = enquiryQrProperties;
        this.enquiryQrGenerator = enquiryQrGenerator;
    }

    @Override
    public boolean isEnquiryQrEnabled() {
        return enquiryQrFeatureGuard.isEnabled();
    }

    @Override
    public String getPublicFormUrl() {
        enquiryQrFeatureGuard.checkFeatureEnabled();

        return enquiryQrProperties
                .getEnquiry()
                .getPublicFormUrl();
    }

    @Override
    public void validateEnquiryQrEnabled() {
        enquiryQrFeatureGuard.checkFeatureEnabled();
    }

    @Override
    public byte[] generateEnquiryQrCode(int width, int height) {

        enquiryQrFeatureGuard.checkFeatureEnabled();

        String publicFormUrl = enquiryQrProperties
                .getEnquiry()
                .getPublicFormUrl();

        return enquiryQrGenerator.generateQrCode(
                publicFormUrl,
                width,
                height
        );
    }
}
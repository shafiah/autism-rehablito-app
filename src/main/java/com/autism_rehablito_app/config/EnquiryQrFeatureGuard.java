package com.autism_rehablito_app.config;

import org.springframework.stereotype.Component;

@Component
public class EnquiryQrFeatureGuard {

    private final EnquiryQrProperties enquiryQrProperties;

    public EnquiryQrFeatureGuard(EnquiryQrProperties enquiryQrProperties) {
        this.enquiryQrProperties = enquiryQrProperties;
    }

    public boolean isEnabled() {
        return enquiryQrProperties
                .getFeature()
                .isEnquiryQrEnabled();
    }

    public void checkFeatureEnabled() {
        if (!isEnabled()) {
            throw new IllegalStateException(
                    "Enquiry QR feature is currently disabled."
            );
        }
    }
}
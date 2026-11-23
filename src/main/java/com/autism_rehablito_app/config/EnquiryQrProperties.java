package com.autism_rehablito_app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class EnquiryQrProperties {

    private Feature feature = new Feature();
    private Enquiry enquiry = new Enquiry();

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Enquiry getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(Enquiry enquiry) {
        this.enquiry = enquiry;
    }

    // Feature configuration
    public static class Feature {

        private boolean enquiryQrEnabled;

        public boolean isEnquiryQrEnabled() {
            return enquiryQrEnabled;
        }

        public void setEnquiryQrEnabled(boolean enquiryQrEnabled) {
            this.enquiryQrEnabled = enquiryQrEnabled;
        }
    }

    // Enquiry configuration
    public static class Enquiry {

        private String publicFormUrl;

        public String getPublicFormUrl() {
            return publicFormUrl;
        }

        public void setPublicFormUrl(String publicFormUrl) {
            this.publicFormUrl = publicFormUrl;
        }
    }
}
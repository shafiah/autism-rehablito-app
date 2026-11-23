package com.autism_rehablito_app.dto.enquiry;

public class AdminEnquiryQrConfigResponse {

    private boolean enabled;
    private String publicFormUrl;

    public AdminEnquiryQrConfigResponse() {
    }

    public AdminEnquiryQrConfigResponse(boolean enabled, String publicFormUrl) {
        this.enabled = enabled;
        this.publicFormUrl = publicFormUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPublicFormUrl() {
        return publicFormUrl;
    }

    public void setPublicFormUrl(String publicFormUrl) {
        this.publicFormUrl = publicFormUrl;
    }
}
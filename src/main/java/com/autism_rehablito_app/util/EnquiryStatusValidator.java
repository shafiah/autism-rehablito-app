package com.autism_rehablito_app.util;

import com.autism_rehablito_app.enums.EnquiryStatus;

public class EnquiryStatusValidator {

    private EnquiryStatusValidator() {
    }

    public static boolean isValidStatus(String status) {

        if (status == null || status.trim().isEmpty()) {
            return false;
        }

        try {
            EnquiryStatus.valueOf(status.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
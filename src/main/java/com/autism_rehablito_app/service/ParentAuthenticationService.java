package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.parent.ParentForgotPasswordRequest;
import com.autism_rehablito_app.dto.parent.ParentForgotPasswordResponse;
import com.autism_rehablito_app.dto.parent.ParentLoginRequest;
import com.autism_rehablito_app.dto.parent.ParentLoginResponse;
import com.autism_rehablito_app.dto.parent.ParentRegisterRequest;
import com.autism_rehablito_app.dto.parent.ParentRegisterResponse;
import com.autism_rehablito_app.dto.parent.ParentResetPasswordRequest;
import com.autism_rehablito_app.dto.parent.ParentVerifyOtpRequest;
import com.autism_rehablito_app.dto.parent.ParentVerifyOtpResponse;

public interface ParentAuthenticationService {

    ParentRegisterResponse register(
            ParentRegisterRequest request
    );

    ParentLoginResponse login(
            ParentLoginRequest request
    );

    ParentForgotPasswordResponse forgotPassword(
            ParentForgotPasswordRequest request
    );

    ParentVerifyOtpResponse verifyOtp(
            ParentVerifyOtpRequest request
    );

    void resetPassword(
            ParentResetPasswordRequest request
    );
}
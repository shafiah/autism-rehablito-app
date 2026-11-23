package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.admin.AdminLoginRequest;
import com.autism_rehablito_app.dto.admin.AdminLoginResponse;

public interface AdminAuthenticationService {

    AdminLoginResponse login(
            AdminLoginRequest request
    );
}
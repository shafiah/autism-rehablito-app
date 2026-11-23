package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.admin.AdminSessionCreateRequest;
import com.autism_rehablito_app.dto.admin.AdminSessionResponse;
import com.autism_rehablito_app.dto.admin.AdminSessionUpdateRequest;

import java.util.List;

public interface AdminSessionService {

    AdminSessionResponse createSession(
            AdminSessionCreateRequest request
    );

    List<AdminSessionResponse> getAllSessions();

    List<AdminSessionResponse> getSessionsByBranch(
            Long branchId
    );

    AdminSessionResponse getSessionById(
            Long sessionId
    );

    AdminSessionResponse updateSession(
            Long sessionId,
            AdminSessionUpdateRequest request
    );

    AdminSessionResponse cancelSession(
            Long sessionId
    );
}
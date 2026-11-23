package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.parent.ParentSessionResponse;

import java.util.List;

public interface ParentSessionService {

    List<ParentSessionResponse> getMyChildSessions(
            Long userId,
            Long childId
    );

    ParentSessionResponse getMyChildSessionById(
            Long userId,
            Long childId,
            Long sessionId
    );
}
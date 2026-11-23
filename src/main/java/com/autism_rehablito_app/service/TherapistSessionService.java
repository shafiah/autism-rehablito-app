package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.therapist.TherapistSessionResponse;

import java.util.List;

public interface TherapistSessionService {

    List<TherapistSessionResponse> getMySessions(Long userId);

    TherapistSessionResponse getMySessionById(
            Long userId,
            Long sessionId
    );

    TherapistSessionResponse startSession(
            Long userId,
            Long sessionId
    );

    TherapistSessionResponse completeSession(
            Long userId,
            Long sessionId
    );
}
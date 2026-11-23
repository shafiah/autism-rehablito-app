package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.therapist.TherapistChildResponse;

import java.util.List;

public interface TherapistChildService {

    List<TherapistChildResponse> getMyChildren(
            Long userId
    );

    TherapistChildResponse getMyChildById(
            Long userId,
            Long childId
    );
}
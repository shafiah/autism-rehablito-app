package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.therapist.TherapistLoginRequest;
import com.autism_rehablito_app.dto.therapist.TherapistLoginResponse;

public interface TherapistAuthenticationService {

    TherapistLoginResponse login(
            TherapistLoginRequest request
    );
}
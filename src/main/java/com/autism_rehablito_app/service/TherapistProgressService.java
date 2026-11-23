
package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.therapist.TherapistProgressRequest;
import com.autism_rehablito_app.dto.therapist.TherapistProgressResponse;

import java.util.List;

public interface TherapistProgressService {

    TherapistProgressResponse createProgress(
            Long userId,
            Long sessionId,
            TherapistProgressRequest request
    );

    List<TherapistProgressResponse> getChildProgress(
            Long userId,
            Long childId
    );
}

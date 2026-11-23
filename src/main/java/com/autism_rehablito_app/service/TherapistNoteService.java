
package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.therapist.TherapistNoteRequest;
import com.autism_rehablito_app.dto.therapist.TherapistNoteResponse;

import java.util.List;

public interface TherapistNoteService {

    TherapistNoteResponse createNote(
            Long userId,
            Long sessionId,
            TherapistNoteRequest request
    );

    List<TherapistNoteResponse> getChildNotes(
            Long userId,
            Long childId
    );
}

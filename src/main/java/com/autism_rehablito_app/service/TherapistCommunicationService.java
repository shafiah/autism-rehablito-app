package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.therapist.TherapistMessageReadResponse;
import com.autism_rehablito_app.dto.therapist.TherapistMessageRequest;
import com.autism_rehablito_app.dto.therapist.TherapistMessageResponse;
import com.autism_rehablito_app.dto.therapist.TherapistUnreadMessageResponse;

import java.util.List;

public interface TherapistCommunicationService {

    TherapistMessageResponse sendMessage(
            Long userId,
            Long childId,
            TherapistMessageRequest request
    );

    List<TherapistMessageResponse> getConversation(
            Long userId,
            Long childId
    );
    
    TherapistMessageReadResponse markMessageAsRead(
            Long userId,
            Long childId,
            Long messageId
    );

    TherapistUnreadMessageResponse getUnreadCount(
            Long userId,
            Long childId
    );
}
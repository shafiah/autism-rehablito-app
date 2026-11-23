package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.admin.AdminAssignedTherapistResponse;
import com.autism_rehablito_app.dto.admin.AdminCommunicationMessageResponse;
import com.autism_rehablito_app.dto.admin.AdminCommunicationSummaryResponse;
import com.autism_rehablito_app.dto.admin.AdminMessageRequest;

import java.util.List;

public interface AdminCommunicationService {

    List<AdminCommunicationMessageResponse> getChildConversation(Long childId);

    AdminCommunicationMessageResponse getMessageById(
            Long childId,
            Long messageId
    );

    AdminCommunicationMessageResponse sendMessage(
            Long userId,
            Long childId,
            AdminMessageRequest request
    );
    
    List<AdminCommunicationSummaryResponse> getCommunicationSummaries();
    
    List<AdminAssignedTherapistResponse> getAssignedTherapists(Long childId);
    
    List<AdminCommunicationMessageResponse> getTherapistConversation(
            Long childId,
            Long therapistUserId
    );
}
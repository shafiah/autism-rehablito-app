package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.parent.ParentMessageReadResponse;
import com.autism_rehablito_app.dto.parent.ParentMessageRequest;
import com.autism_rehablito_app.dto.parent.ParentMessageResponse;
import com.autism_rehablito_app.dto.parent.ParentUnreadMessageResponse;

import java.util.List;

public interface ParentCommunicationService {

    ParentMessageResponse sendMessage(
            Long userId,
            Long childId,
            Long therapistId,
            ParentMessageRequest request
    );

    List<ParentMessageResponse> getConversation(
            Long userId,
            Long childId,
            Long therapistId
    );
    
    ParentMessageReadResponse markMessageAsRead(
            Long userId,
            Long childId,
            Long therapistId,
            Long messageId
    );

    ParentUnreadMessageResponse getUnreadCount(
            Long userId,
            Long childId,
            Long therapistId
    );
}
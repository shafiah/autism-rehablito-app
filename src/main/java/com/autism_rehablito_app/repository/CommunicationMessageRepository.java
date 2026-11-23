package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.CommunicationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunicationMessageRepository
        extends JpaRepository<CommunicationMessage, Long> {

    List<CommunicationMessage> findByChildIdOrderByCreatedAtAsc(
            Long childId
    );

    List<CommunicationMessage> findByChildIdAndReceiverIdOrderByCreatedAtAsc(
            Long childId,
            Long receiverId
    );

    List<CommunicationMessage> findByChildIdAndReceiverIdAndReadFalseOrderByCreatedAtAsc(
            Long childId,
            Long receiverId
    );

    List<CommunicationMessage> findByChildIdAndReceiverIdAndReadFalse(
            Long childId,
            Long receiverId
    );

    long countByReceiverIdAndReadFalse(
            Long receiverId
    );

    long countByChildIdAndReceiverIdAndReadFalse(
            Long childId,
            Long receiverId
    );
    
    List<CommunicationMessage>
    findByChildIdAndReceiverIdAndConversationTherapist_IdAndReadFalse(
            Long childId,
            Long receiverId,
            Long therapistUserId
    );

    long countByChildIdAndReceiverIdAndConversationTherapist_IdAndReadFalse(
            Long childId,
            Long receiverId,
            Long therapistUserId
    );
}
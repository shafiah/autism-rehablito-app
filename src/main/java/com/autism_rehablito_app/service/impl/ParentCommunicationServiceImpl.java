package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.parent.ParentMessageReadResponse;
import com.autism_rehablito_app.dto.parent.ParentMessageRequest;
import com.autism_rehablito_app.dto.parent.ParentMessageResponse;
import com.autism_rehablito_app.dto.parent.ParentUnreadMessageResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapist;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.entity.CommunicationMessage;
import com.autism_rehablito_app.exception.BadRequestException;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ChildTherapistRepository;
import com.autism_rehablito_app.repository.CommunicationMessageRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.UserRepository;
import com.autism_rehablito_app.service.ParentCommunicationService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParentCommunicationServiceImpl
        implements ParentCommunicationService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final TherapistRepository therapistRepository;
    private final ChildTherapistRepository childTherapistRepository;
    private final CommunicationMessageRepository communicationMessageRepository;
    private final UserRepository userRepository;

    public ParentCommunicationServiceImpl(
            ParentRepository parentRepository,
            ChildRepository childRepository,
            TherapistRepository therapistRepository,
            ChildTherapistRepository childTherapistRepository,
            CommunicationMessageRepository communicationMessageRepository,
            UserRepository userRepository) {

        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.therapistRepository = therapistRepository;
        this.childTherapistRepository = childTherapistRepository;
        this.communicationMessageRepository = communicationMessageRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ParentMessageResponse sendMessage(
            Long userId,
            Long childId,
            Long therapistId,
            ParentMessageRequest request) {

        // 1. Find Parent profile
        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Parent profile not found"));

        User parentUser = parent.getUser();

        if (!Boolean.TRUE.equals(parentUser.getActive())
                || !Boolean.TRUE.equals(parent.getActive())) {

            throw new BadRequestException(
                    "Parent account is inactive");
        }

        // 2. Find Child
        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Child not found"));

        // 3. Verify Child belongs to logged-in Parent
        if (!child.getParent().getId().equals(parent.getId())) {
            throw new BadRequestException(
                    "You are not authorized to access this child");
        }

        // 4. Child must be active
        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new BadRequestException(
                    "Child is inactive");
        }

        // 5. Find Therapist
        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Therapist not found"));

        // 6. Therapist must be active
        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(therapist.getUser().getActive())) {

            throw new BadRequestException(
                    "Therapist is inactive");
        }

        // 7. Verify active Child-Therapist assignment
        ChildTherapist assignment =
                childTherapistRepository
                        .findByChildIdAndTherapistIdAndActive(
                                childId,
                                therapistId,
                                true
                        )
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "Therapist is not assigned to this child"));

        // Prevent unused variable warning / future readability
        if (assignment.getId() == null) {
            throw new BadRequestException(
                    "Invalid child-therapist assignment");
        }

        // 8. Therapist User
        User therapistUser = therapist.getUser();

        // 9. Create message
        CommunicationMessage communicationMessage =
                new CommunicationMessage();

        communicationMessage.setChild(child);
        communicationMessage.setSender(parentUser);
        communicationMessage.setReceiver(therapistUser);
        communicationMessage.setConversationTherapist(therapistUser);
        communicationMessage.setMessage(
                request.getMessage().trim()
        );
        communicationMessage.setRead(false);

        CommunicationMessage savedMessage =
                communicationMessageRepository.save(
                        communicationMessage
                );

        return mapToResponse(savedMessage);
    }

    private ParentMessageResponse mapToResponse(
            CommunicationMessage message) {

        ParentMessageResponse response =
                new ParentMessageResponse();

        response.setMessageId(message.getId());

        response.setChildId(
                message.getChild().getId()
        );

        response.setChildName(
                message.getChild().getChildName()
        );

        response.setSenderUserId(
                message.getSender().getId()
        );

        response.setSenderName(
                message.getSender().getName()
        );

        response.setReceiverUserId(
                message.getReceiver().getId()
        );

        response.setReceiverName(
                message.getReceiver().getName()
        );

        response.setMessage(
                message.getMessage()
        );

        response.setRead(
                message.getRead()
        );

        response.setCreatedAt(
                message.getCreatedAt()
        );

        response.setUpdatedAt(
                message.getUpdatedAt()
        );

        return response;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ParentMessageResponse> getConversation(
            Long userId,
            Long childId,
            Long therapistId) {

        // 1. Find Parent
        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Parent profile not found"));

        User parentUser = parent.getUser();

        // 2. Parent must be active
        if (!Boolean.TRUE.equals(parentUser.getActive())
                || !Boolean.TRUE.equals(parent.getActive())) {

            throw new BadRequestException(
                    "Parent account is inactive");
        }

        // 3. Find Child
        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Child not found"));

        // 4. Verify child belongs to parent
        if (!child.getParent().getId().equals(parent.getId())) {
            throw new BadRequestException(
                    "You are not authorized to access this child");
        }

        // 5. Child must be active
        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new BadRequestException(
                    "Child is inactive");
        }

        // 6. Find Therapist
        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Therapist not found"));

        // 7. Therapist must be active
        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(therapist.getUser().getActive())) {

            throw new BadRequestException(
                    "Therapist is inactive");
        }

        // 8. Verify active child-therapist assignment
        childTherapistRepository
                .findByChildIdAndTherapistIdAndActive(
                        childId,
                        therapistId,
                        true
                )
                .orElseThrow(() ->
                        new BadRequestException(
                                "Therapist is not assigned to this child"));


     // 9. Get messages for this child
        List<CommunicationMessage> messages =
                communicationMessageRepository
                        .findByChildIdOrderByCreatedAtAsc(childId);

        // 10. Identify logged-in Parent and selected Therapist
        Long parentUserId = parentUser.getId();
        Long selectedTherapistUserId = therapist.getUser().getId();

        // 11. Filter exact selected Therapist conversation
        return messages.stream()
                .filter(message -> {

                    // Conversation must belong to selected Therapist
                    if (message.getConversationTherapist() == null
                            || !message.getConversationTherapist()
                            .getId()
                            .equals(selectedTherapistUserId)) {

                        return false;
                    }

                    Long senderId = message.getSender().getId();
                    Long receiverId = message.getReceiver().getId();

                    // Parent <-> Selected Therapist
                    boolean parentTherapistMessage =
                            (senderId.equals(parentUserId)
                                    && receiverId.equals(selectedTherapistUserId))
                            ||
                            (senderId.equals(selectedTherapistUserId)
                                    && receiverId.equals(parentUserId));

                    // Admin -> This Parent
                    boolean adminToParent =
                            message.getSender().getRole().name().equals("ADMIN")
                                    && receiverId.equals(parentUserId);

                    // Admin -> Selected Therapist
                    boolean adminToSelectedTherapist =
                            message.getSender().getRole().name().equals("ADMIN")
                                    && receiverId.equals(selectedTherapistUserId);

                    return parentTherapistMessage
                            || adminToParent
                            || adminToSelectedTherapist;
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        }
    

@Override
@Transactional
public ParentMessageReadResponse markMessageAsRead(
        Long userId,
        Long childId,
        Long therapistId,
        Long messageId) {

    // 1. Find Parent profile
    Parent parent = parentRepository.findByUserId(userId)
            .orElseThrow(() ->
                    new BadRequestException("Parent not found"));

    User parentUser = parent.getUser();

    // 2. Find Child
    Child child = childRepository.findById(childId)
            .orElseThrow(() ->
                    new BadRequestException("Child not found"));

    // 3. Verify child belongs to Parent
    if (child.getParent() == null ||
            !child.getParent().getId().equals(parent.getId())) {
        throw new BadRequestException(
                "You are not authorized for this child");
    }

    // 4. Find Therapist
    Therapist therapist = therapistRepository.findById(therapistId)
            .orElseThrow(() ->
                    new BadRequestException("Therapist not found"));

    User therapistUser = therapist.getUser();

    if (therapistUser == null) {
        throw new BadRequestException("Therapist user not found");
    }

    // 5. Verify active assignment
    childTherapistRepository
            .findByChildIdAndTherapistIdAndActive(
                    childId, therapistId, true)
            .orElseThrow(() ->
                    new BadRequestException(
                            "Therapist is not assigned to this child"));

    // 6. Find message
    CommunicationMessage message =
            communicationMessageRepository.findById(messageId)
                    .orElseThrow(() ->
                            new BadRequestException("Message not found"));

    // 7. Verify message belongs to child
    if (message.getChild() == null ||
            !message.getChild().getId().equals(childId)) {
        throw new BadRequestException(
                "Message does not belong to this child");
    }

    // 8. Only Parent's received messages can be marked read
    if (message.getReceiver() == null ||
            !message.getReceiver().getId().equals(parentUser.getId())) {
        throw new BadRequestException(
                "This message is not addressed to this parent");
    }

    // 9. Verify selected Therapist conversation
    if (message.getConversationTherapist() == null ||
            !message.getConversationTherapist()
                    .getId().equals(therapistUser.getId())) {
        throw new BadRequestException(
                "Message does not belong to this therapist conversation");
    }

    // 10. Mark as read
    message.setRead(true);

    communicationMessageRepository.save(message);

    ParentMessageReadResponse response =
            new ParentMessageReadResponse();

    response.setMessageId(message.getId());
    response.setRead(message.getRead());

    return response;
}

@Override
@Transactional(readOnly = true)
public ParentUnreadMessageResponse getUnreadCount(
        Long userId,
        Long childId,
        Long therapistId) {

    // 1. Find Parent profile
    Parent parent = parentRepository.findByUserId(userId)
            .orElseThrow(() ->
                    new BadRequestException("Parent not found"));

    User parentUser = parent.getUser();

    // 2. Find Child
    Child child = childRepository.findById(childId)
            .orElseThrow(() ->
                    new BadRequestException("Child not found"));

    // 3. Verify child belongs to Parent profile
    if (child.getParent() == null ||
            !child.getParent().getId().equals(parent.getId())) {
        throw new BadRequestException(
                "You are not authorized for this child");
    }

    // 4. Find Therapist
    Therapist therapist = therapistRepository.findById(therapistId)
            .orElseThrow(() ->
                    new BadRequestException("Therapist not found"));

    User therapistUser = therapist.getUser();

    if (therapistUser == null) {
        throw new BadRequestException("Therapist user not found");
    }

    // 5. Verify active assignment
    childTherapistRepository
            .findByChildIdAndTherapistIdAndActive(
                    childId, therapistId, true)
            .orElseThrow(() ->
                    new BadRequestException(
                            "Therapist is not assigned to this child"));

    // 6. Get unread count for selected Therapist conversation
    long unreadCount =
            communicationMessageRepository
                    .countByChildIdAndReceiverIdAndConversationTherapist_IdAndReadFalse(
                            childId,
                            parentUser.getId(),
                            therapistUser.getId()
                    );

    // 7. Prepare response
    ParentUnreadMessageResponse response =
            new ParentUnreadMessageResponse();

    response.setChildId(childId);
    response.setTherapistId(therapistId);
    response.setUnreadCount(unreadCount);

    return response;
}

}
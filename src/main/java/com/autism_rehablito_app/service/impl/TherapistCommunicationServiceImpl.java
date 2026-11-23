
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.therapist.TherapistMessageReadResponse;
import com.autism_rehablito_app.dto.therapist.TherapistMessageRequest;
import com.autism_rehablito_app.dto.therapist.TherapistMessageResponse;
import com.autism_rehablito_app.dto.therapist.TherapistUnreadMessageResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapist;
import com.autism_rehablito_app.entity.CommunicationMessage;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.exception.BadRequestException;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ChildTherapistRepository;
import com.autism_rehablito_app.repository.CommunicationMessageRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.service.TherapistCommunicationService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TherapistCommunicationServiceImpl
        implements TherapistCommunicationService {

    private final TherapistRepository therapistRepository;
    private final ChildRepository childRepository;
    private final ChildTherapistRepository childTherapistRepository;
    private final CommunicationMessageRepository communicationMessageRepository;

    public TherapistCommunicationServiceImpl(
            TherapistRepository therapistRepository,
            ChildRepository childRepository,
            ChildTherapistRepository childTherapistRepository,
            CommunicationMessageRepository communicationMessageRepository) {

        this.therapistRepository = therapistRepository;
        this.childRepository = childRepository;
        this.childTherapistRepository = childTherapistRepository;
        this.communicationMessageRepository = communicationMessageRepository;
    }

    @Override
    @Transactional
    public TherapistMessageResponse sendMessage(
            Long userId,
            Long childId,
            TherapistMessageRequest request) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist profile not found"));

        User therapistUser = therapist.getUser();

        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(therapistUser.getActive())) {
            throw new BadRequestException("Therapist account is inactive");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException("Child not found"));

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new BadRequestException("Child is inactive");
        }

        ChildTherapist assignment = childTherapistRepository
                .findByChildIdAndTherapistIdAndActive(
                        childId, therapist.getId(), true)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Therapist is not assigned to this child"));

        if (assignment.getId() == null) {
            throw new BadRequestException(
                    "Invalid child-therapist assignment");
        }

        Parent parent = child.getParent();

        if (parent == null) {
            throw new BadRequestException(
                    "Parent not found for this child");
        }

        if (!Boolean.TRUE.equals(parent.getActive())
                || !Boolean.TRUE.equals(parent.getUser().getActive())) {
            throw new BadRequestException("Parent account is inactive");
        }

        User parentUser = parent.getUser();

        CommunicationMessage communicationMessage =
                new CommunicationMessage();

        communicationMessage.setChild(child);
        communicationMessage.setSender(therapistUser);
        communicationMessage.setReceiver(parentUser);
        communicationMessage.setConversationTherapist(therapistUser);
        communicationMessage.setMessage(request.getMessage().trim());
        communicationMessage.setRead(false);

        CommunicationMessage savedMessage =
                communicationMessageRepository.save(communicationMessage);

        return mapToResponse(savedMessage);
    }

    private TherapistMessageResponse mapToResponse(
            CommunicationMessage message) {

        TherapistMessageResponse response =
                new TherapistMessageResponse();

        response.setMessageId(message.getId());
        response.setChildId(message.getChild().getId());
        response.setChildName(message.getChild().getChildName());
        response.setSenderUserId(message.getSender().getId());
        response.setSenderName(message.getSender().getName());
        response.setReceiverUserId(message.getReceiver().getId());
        response.setReceiverName(message.getReceiver().getName());
        response.setMessage(message.getMessage());
        response.setRead(message.getRead());
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TherapistMessageResponse> getConversation(
            Long userId,
            Long childId) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist profile not found"));

        User therapistUser = therapist.getUser();

        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(therapistUser.getActive())) {
            throw new BadRequestException("Therapist account is inactive");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException("Child not found"));

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new BadRequestException("Child is inactive");
        }

        childTherapistRepository
                .findByChildIdAndTherapistIdAndActive(
                        childId, therapist.getId(), true)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Therapist is not assigned to this child"));

        Parent parent = child.getParent();

        if (parent == null) {
            throw new BadRequestException("Parent not found for this child");
        }

        if (!Boolean.TRUE.equals(parent.getActive())
                || !Boolean.TRUE.equals(parent.getUser().getActive())) {
            throw new BadRequestException("Parent account is inactive");
        }

        List<CommunicationMessage> messages =
                communicationMessageRepository
                        .findByChildIdOrderByCreatedAtAsc(childId);

        return messages.stream()
                .filter(message ->
                        message.getConversationTherapist() != null
                                && message.getConversationTherapist()
                                        .getId().equals(therapistUser.getId()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TherapistMessageReadResponse markMessageAsRead(
            Long userId,
            Long childId,
            Long messageId) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist profile not found"));

        User therapistUser = therapist.getUser();

        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(therapistUser.getActive())) {
            throw new BadRequestException("Therapist account is inactive");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException("Child not found"));

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new BadRequestException("Child is inactive");
        }

        childTherapistRepository
                .findByChildIdAndTherapistIdAndActive(
                        childId, therapist.getId(), true)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Therapist is not assigned to this child"));

        CommunicationMessage message =
                communicationMessageRepository.findById(messageId)
                        .orElseThrow(() ->
                                new BadRequestException("Message not found"));

        if (!message.getChild().getId().equals(childId)) {
            throw new BadRequestException(
                    "Message does not belong to this child");
        }

        // Only messages addressed to this Therapist can be marked read.
        if (message.getReceiver() == null
                || !message.getReceiver().getId().equals(therapistUser.getId())) {
            throw new BadRequestException(
                    "You can only mark received messages as read");
        }

        // Ensure the message belongs to this Therapist's conversation.
        if (message.getConversationTherapist() == null
                || !message.getConversationTherapist().getId()
                        .equals(therapistUser.getId())) {
            throw new BadRequestException(
                    "Message does not belong to this therapist conversation");
        }

        message.setRead(true);

        CommunicationMessage savedMessage =
                communicationMessageRepository.save(message);

        TherapistMessageReadResponse response =
                new TherapistMessageReadResponse();

        response.setMessageId(savedMessage.getId());
        response.setRead(savedMessage.getRead());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public TherapistUnreadMessageResponse getUnreadCount(
            Long userId,
            Long childId) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist profile not found"));

        User therapistUser = therapist.getUser();

        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(therapistUser.getActive())) {
            throw new BadRequestException("Therapist account is inactive");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException("Child not found"));

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new BadRequestException("Child is inactive");
        }

        childTherapistRepository
                .findByChildIdAndTherapistIdAndActive(
                        childId, therapist.getId(), true)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Therapist is not assigned to this child"));

        long unreadCount = communicationMessageRepository
                .countByChildIdAndReceiverIdAndConversationTherapist_IdAndReadFalse(
                        childId,
                        therapistUser.getId(),
                        therapistUser.getId());

        TherapistUnreadMessageResponse response =
                new TherapistUnreadMessageResponse();

        response.setChildId(childId);
        response.setUnreadCount(unreadCount);

        return response;
    }
}

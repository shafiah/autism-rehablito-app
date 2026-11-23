
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.admin.AdminAssignedTherapistResponse;
import com.autism_rehablito_app.dto.admin.AdminCommunicationMessageResponse;
import com.autism_rehablito_app.dto.admin.AdminCommunicationSummaryResponse;
import com.autism_rehablito_app.dto.admin.AdminMessageRequest;
import com.autism_rehablito_app.entity.Branch;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapist;
import com.autism_rehablito_app.entity.CommunicationMessage;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.Role;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.exception.BadRequestException;
import com.autism_rehablito_app.exception.ResourceNotFoundException;
import com.autism_rehablito_app.repository.BranchRepository;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ChildTherapistRepository;
import com.autism_rehablito_app.repository.CommunicationMessageRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.UserRepository;
import com.autism_rehablito_app.service.AdminCommunicationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AdminCommunicationServiceImpl
        implements AdminCommunicationService {

    private final ChildRepository childRepository;
    private final CommunicationMessageRepository communicationMessageRepository;
    private final UserRepository userRepository;
    private final ChildTherapistRepository childTherapistRepository;
    private final TherapistRepository therapistRepository;
    private final ParentRepository parentRepository;
    private final BranchRepository branchRepository;
    
    private static final Logger log =
            LoggerFactory.getLogger(AdminCommunicationServiceImpl.class);

    public AdminCommunicationServiceImpl(
            ChildRepository childRepository,
            CommunicationMessageRepository communicationMessageRepository,
            UserRepository userRepository,
            ChildTherapistRepository childTherapistRepository,
            TherapistRepository therapistRepository,
            ParentRepository parentRepository,
            BranchRepository branchRepository) {

        this.childRepository = childRepository;
        this.communicationMessageRepository =
                communicationMessageRepository;
        this.userRepository = userRepository;
        this.childTherapistRepository = childTherapistRepository;
        this.therapistRepository = therapistRepository;
        this.parentRepository = parentRepository;
        this.branchRepository = branchRepository;
    }

    /*
     * Admin communication list summary.
     */
    
    @Override
    @Transactional(readOnly = true)
    public List<AdminCommunicationSummaryResponse>
    getCommunicationSummaries() {

        log.info("ADMIN_COMMUNICATION: Summary API started");

        try {
            List<Child> children = childRepository.findAll();

            log.info("ADMIN_COMMUNICATION: Total children found: {}",
                    children.size());

            List<AdminCommunicationSummaryResponse> result =
                    children.stream()
                            .filter(child ->
                                    Boolean.TRUE.equals(child.getActive()))
                            .map(child -> {
                                log.info(
                                        "ADMIN_COMMUNICATION: Processing childId={}, childName={}",
                                        child.getId(),
                                        child.getChildName()
                                );

                                return mapToSummaryResponse(child);
                            })
                            .toList();

            log.info(
                    "ADMIN_COMMUNICATION: Summary API completed. Active summaries: {}",
                    result.size()
            );

            return result;

        } catch (Exception e) {

            log.error(
                    "ADMIN_COMMUNICATION: Error while generating summaries",
                    e
            );

            throw e;
        }
    }
    
//    @Override
//    @Transactional(readOnly = true)
//    public List<AdminCommunicationSummaryResponse>
//    getCommunicationSummaries() {
//
//        return childRepository.findAll()
//                .stream()
//                .filter(child ->
//                        Boolean.TRUE.equals(child.getActive()))
//                .map(this::mapToSummaryResponse)
//                .toList();
//    }
//

private AdminCommunicationSummaryResponse mapToSummaryResponse(
        Child child) {

    AdminCommunicationSummaryResponse response =
            new AdminCommunicationSummaryResponse();

    response.setChildId(child.getId());
    response.setChildName(child.getChildName());

    // 1. Parent details
    Parent parent = child.getParent();

    if (parent != null) {
        response.setParentId(parent.getId());

        User parentUser = parent.getUser();

        if (parentUser != null) {
            response.setParentUserId(parentUser.getId());
            response.setParentName(parentUser.getName());
        }
    }

    // 2. Branch details
    Branch branch = child.getBranch();

    if (branch != null) {
        response.setBranchId(branch.getId());
        response.setBranchName(branch.getBranchName());
    }

    // 3. Find first active Therapist assignment
    ChildTherapist activeAssignment =
            childTherapistRepository
                    .findByChildIdAndActive(child.getId(), true)
                    .stream()
                    .filter(assignment ->
                            assignment.getTherapist() != null)
                    .filter(assignment ->
                            Boolean.TRUE.equals(
                                    assignment.getTherapist().getActive()))
                    .filter(assignment ->
                            assignment.getTherapist().getUser() != null)
                    .filter(assignment ->
                            Boolean.TRUE.equals(
                                    assignment.getTherapist()
                                            .getUser().getActive()))
                    .findFirst()
                    .orElse(null);

    if (activeAssignment != null) {

        Therapist therapist = activeAssignment.getTherapist();
        User therapistUser = therapist.getUser();

        response.setTherapistId(therapist.getId());
        response.setTherapistUserId(therapistUser.getId());
        response.setTherapistName(therapistUser.getName());

        // 4. Parent unread count for selected Therapist conversation
        if (parent != null && parent.getUser() != null) {

            response.setParentUnreadCount(
                    communicationMessageRepository
                            .countByChildIdAndReceiverIdAndConversationTherapist_IdAndReadFalse(
                                    child.getId(),
                                    parent.getUser().getId(),
                                    therapistUser.getId()
                            )
            );

        } else {
            response.setParentUnreadCount(0L);
        }

        // 5. Selected Therapist unread count
        response.setTherapistUnreadCount(
                communicationMessageRepository
                        .countByChildIdAndReceiverIdAndConversationTherapist_IdAndReadFalse(
                                child.getId(),
                                therapistUser.getId(),
                                therapistUser.getId()
                        )
        );

    } else {

        response.setParentUnreadCount(0L);
        response.setTherapistUnreadCount(0L);
    }

    // 6. Retrieve latest message for this child
    List<CommunicationMessage> messages =
            communicationMessageRepository
                    .findByChildIdOrderByCreatedAtAsc(child.getId());

    if (!messages.isEmpty()) {

        CommunicationMessage latestMessage =
                messages.get(messages.size() - 1);

        response.setLatestMessageId(latestMessage.getId());
        response.setLatestMessage(latestMessage.getMessage());
        response.setLatestMessageAt(latestMessage.getCreatedAt());

        User sender = latestMessage.getSender();
        User receiver = latestMessage.getReceiver();

        if (sender != null) {
            response.setLatestSenderUserId(sender.getId());
            response.setLatestSenderName(sender.getName());

            if (sender.getRole() != null) {
                response.setLatestSenderRole(sender.getRole().name());
            }
        }

        if (receiver != null) {
            response.setLatestReceiverUserId(receiver.getId());
            response.setLatestReceiverName(receiver.getName());

            if (receiver.getRole() != null) {
                response.setLatestReceiverRole(receiver.getRole().name());
            }
        }
    }

    return response;
}

    @Override
    @Transactional(readOnly = true)
    public List<AdminCommunicationMessageResponse> getChildConversation(
            Long childId) {

        Child child = getActiveChild(childId);

        return communicationMessageRepository
                .findByChildIdOrderByCreatedAtAsc(child.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminCommunicationMessageResponse getMessageById(
            Long childId,
            Long messageId) {

        Child child = getActiveChild(childId);

        CommunicationMessage message =
                communicationMessageRepository.findById(messageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Communication message not found"
                                ));

        if (!message.getChild().getId().equals(child.getId())) {
            throw new BadRequestException(
                    "Message does not belong to this child"
            );
        }

        return mapToResponse(message);
    }


//@Override
//@Transactional
//public AdminCommunicationMessageResponse sendMessage(
//        Long userId,
//        Long childId,
//        AdminMessageRequest request) {
//
//    Child child = getActiveChild(childId);
//
//    User admin = userRepository.findById(userId)
//            .orElseThrow(() ->
//                    new ResourceNotFoundException(
//                            "Admin user not found"
//                    ));
//
//    if (!Boolean.TRUE.equals(admin.getActive())) {
//        throw new BadRequestException(
//                "Admin account is inactive"
//        );
//    }
//
//    if (admin.getRole() != Role.ADMIN) {
//        throw new BadRequestException(
//                "Only admin can send admin communication"
//        );
//    }
//
//    User receiver = userRepository
//            .findById(request.getReceiverUserId())
//            .orElseThrow(() ->
//                    new ResourceNotFoundException(
//                            "Receiver user not found"
//                    ));
//
//    if (!Boolean.TRUE.equals(receiver.getActive())) {
//        throw new BadRequestException(
//                "Receiver account is inactive"
//        );
//    }
//
//    if (receiver.getRole() != Role.PARENT
//            && receiver.getRole() != Role.THERAPIST) {
//
//        throw new BadRequestException(
//                "Admin can communicate only with parent or therapist"
//        );
//    }
//
//    validateReceiverBelongsToChildConversation(
//            child,
//            receiver
//    );
//
//    // Identify the Therapist conversation.
//    User conversationTherapist;
//
//    if (receiver.getRole() == Role.THERAPIST) {
//
//        // Admin -> Therapist: conversation belongs to receiver.
//        conversationTherapist = receiver;
//
//    } else {
//
//        // Admin -> Parent: selected Therapist ID is required.
//        Long therapistUserId =
//                request.getConversationTherapistUserId();
//
//        if (therapistUserId == null) {
//            throw new BadRequestException(
//                    "Conversation therapist user id is required when messaging parent"
//            );
//        }
//
//        conversationTherapist = userRepository
//                .findById(therapistUserId)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "Conversation therapist not found"
//                        ));
//
//        if (conversationTherapist.getRole() != Role.THERAPIST
//                || !Boolean.TRUE.equals(conversationTherapist.getActive())) {
//
//            throw new BadRequestException(
//                    "Invalid or inactive conversation therapist"
//            );
//        }
//
//        Therapist therapist = therapistRepository
//                .findByUserId(conversationTherapist.getId())
//                .orElseThrow(() ->
//                        new BadRequestException(
//                                "Therapist profile not found"
//                        ));
//
//        if (!Boolean.TRUE.equals(therapist.getActive())) {
//            throw new BadRequestException(
//                    "Therapist profile is inactive"
//            );
//        }
//
//        boolean assigned = childTherapistRepository
//                .findByChildIdAndTherapistIdAndActive(
//                        child.getId(),
//                        therapist.getId(),
//                        true
//                )
//                .isPresent();
//
//        if (!assigned) {
//            throw new BadRequestException(
//                    "Selected therapist is not assigned to this child"
//            );
//        }
//    }
//
//    CommunicationMessage communicationMessage =
//            new CommunicationMessage();
//
//    communicationMessage.setChild(child);
//    communicationMessage.setSender(admin);
//    communicationMessage.setReceiver(receiver);
//
//    // NEW: Store the exact conversation Therapist User ID.
//    communicationMessage.setConversationTherapist(
//            conversationTherapist
//    );
//
//    communicationMessage.setMessage(
//            request.getMessage().trim()
//    );
//
//    communicationMessage.setRead(false);
//
//    CommunicationMessage saved =
//            communicationMessageRepository.save(
//                    communicationMessage
//            );
//
//    return mapToResponse(saved);
//}
    
    @Override
    @Transactional
    public AdminCommunicationMessageResponse sendMessage(
            Long userId,
            Long childId,
            AdminMessageRequest request) {

        // 1. Validate active child
        Child child = getActiveChild(childId);

        // 2. Validate Admin
        User admin = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Admin user not found"
                        ));

        if (!Boolean.TRUE.equals(admin.getActive())) {
            throw new BadRequestException(
                    "Admin account is inactive"
            );
        }

        if (admin.getRole() != Role.ADMIN) {
            throw new BadRequestException(
                    "Only admin can send admin communication"
            );
        }

        // 3. Selected conversation therapist is mandatory
        Long therapistUserId =
                request.getConversationTherapistUserId();

        if (therapistUserId == null) {
            throw new BadRequestException(
                    "Conversation therapist user id is required"
            );
        }

        // 4. Validate selected therapist User
        User conversationTherapist =
                userRepository.findById(therapistUserId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Conversation therapist not found"
                                ));

        if (conversationTherapist.getRole() != Role.THERAPIST
                || !Boolean.TRUE.equals(
                        conversationTherapist.getActive())) {

            throw new BadRequestException(
                    "Invalid or inactive conversation therapist"
            );
        }

        // 5. Validate therapist profile
        Therapist therapist = therapistRepository
                .findByUserId(therapistUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Therapist profile not found"
                        ));

        if (!Boolean.TRUE.equals(therapist.getActive())) {
            throw new BadRequestException(
                    "Therapist profile is inactive"
            );
        }

        // 6. Validate active assignment with selected child
        boolean assigned = childTherapistRepository
                .findByChildIdAndTherapistIdAndActive(
                        child.getId(),
                        therapist.getId(),
                        true
                )
                .isPresent();

        if (!assigned) {
            throw new BadRequestException(
                    "Selected therapist is not assigned to this child"
            );
        }

        // 7. Validate receiver
        User receiver = userRepository
                .findById(request.getReceiverUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Receiver user not found"
                        ));

        if (!Boolean.TRUE.equals(receiver.getActive())) {
            throw new BadRequestException(
                    "Receiver account is inactive"
            );
        }

        if (receiver.getRole() != Role.PARENT
                && receiver.getRole() != Role.THERAPIST) {

            throw new BadRequestException(
                    "Admin can communicate only with parent or selected therapist"
            );
        }

        // 8. Validate receiver belongs to this child
        validateReceiverBelongsToChildConversation(
                child,
                receiver
        );

        // If receiver is a Therapist, it must be the selected Therapist.
        if (receiver.getRole() == Role.THERAPIST
                && !receiver.getId().equals(therapistUserId)) {

            throw new BadRequestException(
                    "Receiver therapist must match selected conversation therapist"
            );
        }

        // 9. Save message with selected conversation identity
        CommunicationMessage communicationMessage =
                new CommunicationMessage();

        communicationMessage.setChild(child);
        communicationMessage.setSender(admin);
        communicationMessage.setReceiver(receiver);

        // Critical: same conversation identity for Parent,
        // selected Therapist and Admin.
        communicationMessage.setConversationTherapist(
                conversationTherapist
        );

        communicationMessage.setMessage(
                request.getMessage().trim()
        );

        communicationMessage.setRead(false);

        CommunicationMessage saved =
                communicationMessageRepository.save(
                        communicationMessage
                );

        return mapToResponse(saved);
    }


    private Child getActiveChild(Long childId) {

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Child not found"
                        ));

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new BadRequestException(
                    "Child account is inactive"
            );
        }

        return child;
    }

    private void validateReceiverBelongsToChildConversation(
            Child child,
            User receiver) {

        if (receiver.getRole() == Role.PARENT) {

            if (child.getParent() == null
                    || child.getParent().getUser() == null
                    || !child.getParent()
                    .getUser()
                    .getId()
                    .equals(receiver.getId())) {

                throw new BadRequestException(
                        "Parent is not linked with this child"
                );
            }

            return;
        }

        if (receiver.getRole() == Role.THERAPIST) {

            Therapist therapist = therapistRepository
                    .findByUserId(receiver.getId())
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Therapist profile not found"
                            ));

            boolean assigned =
                    childTherapistRepository
                            .findByChildIdAndTherapistIdAndActive(
                                    child.getId(),
                                    therapist.getId(),
                                    true
                            )
                            .isPresent();

            if (!assigned) {
                throw new BadRequestException(
                        "Therapist is not assigned to this child"
                );
            }
        }
    }

    private AdminCommunicationMessageResponse mapToResponse(
            CommunicationMessage message) {

        AdminCommunicationMessageResponse response =
                new AdminCommunicationMessageResponse();

        response.setMessageId(message.getId());
        response.setChildId(message.getChild().getId());
        response.setChildName(message.getChild().getChildName());

        response.setSenderUserId(message.getSender().getId());
        response.setSenderUserName(message.getSender().getName());

        response.setReceiverUserId(message.getReceiver().getId());
        response.setReceiverUserName(message.getReceiver().getName());

        response.setSenderRole(
                message.getSender().getRole().name()
        );

        response.setReceiverRole(
                message.getReceiver().getRole().name()
        );

        response.setMessage(message.getMessage());
        response.setRead(message.getRead());
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());

        return response;
    }
    
    @Override
    public List<AdminAssignedTherapistResponse> getAssignedTherapists(Long childId) {

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new RuntimeException("Child is inactive");
        }

        List<ChildTherapist> assignments =
                childTherapistRepository.findByChildIdAndActive(childId, true);

        return assignments.stream()
                .filter(assignment -> assignment.getTherapist() != null)
                .filter(assignment ->
                        Boolean.TRUE.equals(assignment.getTherapist().getActive()))
                .filter(assignment ->
                        assignment.getTherapist().getUser() != null)
                .filter(assignment ->
                        Boolean.TRUE.equals(
                                assignment.getTherapist().getUser().getActive()))
                .map(assignment -> {
                    Therapist therapist = assignment.getTherapist();
                    User user = therapist.getUser();

                    return new AdminAssignedTherapistResponse(
                            therapist.getId(),
                            user.getId(),
                            user.getName(),
                            therapist.getTherapyType() != null
                                    ? therapist.getTherapyType().toString()
                                    : null
                    );
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AdminCommunicationMessageResponse> getTherapistConversation(
            Long childId,
            Long therapistUserId) {

        // 1. Validate active child
        Child child = getActiveChild(childId);

        // 2. Validate selected therapist user
        User therapistUser = userRepository.findById(therapistUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Therapist user not found"
                        ));

        if (therapistUser.getRole() != Role.THERAPIST
                || !Boolean.TRUE.equals(therapistUser.getActive())) {

            throw new BadRequestException(
                    "Invalid or inactive therapist user"
            );
        }

        // 3. Validate therapist profile
        Therapist therapist = therapistRepository
                .findByUserId(therapistUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Therapist profile not found"
                        ));

        if (!Boolean.TRUE.equals(therapist.getActive())) {
            throw new BadRequestException(
                    "Therapist profile is inactive"
            );
        }

        // 4. Validate active child-therapist assignment
        boolean assigned = childTherapistRepository
                .findByChildIdAndTherapistIdAndActive(
                        child.getId(),
                        therapist.getId(),
                        true
                )
                .isPresent();

        if (!assigned) {
            throw new BadRequestException(
                    "Selected therapist is not assigned to this child"
            );
        }

        // 5. Return only this therapist's conversation messages
        return communicationMessageRepository
                .findByChildIdOrderByCreatedAtAsc(child.getId())
                .stream()
                .filter(message ->
                        message.getConversationTherapist() != null
                )
                .filter(message ->
                        message.getConversationTherapist()
                                .getId()
                                .equals(therapistUserId)
                )
                .map(this::mapToResponse)
                .toList();
    }
}

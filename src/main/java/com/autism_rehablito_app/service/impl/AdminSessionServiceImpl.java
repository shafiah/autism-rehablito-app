package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.admin.AdminSessionCreateRequest;
import com.autism_rehablito_app.dto.admin.AdminSessionResponse;
import com.autism_rehablito_app.dto.admin.AdminSessionUpdateRequest;
import com.autism_rehablito_app.entity.Branch;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapist;
import com.autism_rehablito_app.entity.SessionStatus;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.TherapySession;
import com.autism_rehablito_app.repository.BranchRepository;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ChildTherapistRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.TherapySessionRepository;
import com.autism_rehablito_app.service.AdminSessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.autism_rehablito_app.entity.NotificationType;
import com.autism_rehablito_app.service.NotificationEventService;

@Service
public class AdminSessionServiceImpl implements AdminSessionService {

    private final TherapySessionRepository therapySessionRepository;
    private final ChildRepository childRepository;
    private final TherapistRepository therapistRepository;
    private final BranchRepository branchRepository;
    private final ChildTherapistRepository childTherapistRepository;
    private final NotificationEventService notificationEventService;

    public AdminSessionServiceImpl(
            TherapySessionRepository therapySessionRepository,
            ChildRepository childRepository,
            TherapistRepository therapistRepository,
            BranchRepository branchRepository,
            ChildTherapistRepository childTherapistRepository,
            NotificationEventService notificationEventService) {

        this.therapySessionRepository = therapySessionRepository;
        this.childRepository = childRepository;
        this.therapistRepository = therapistRepository;
        this.branchRepository = branchRepository;
        this.childTherapistRepository = childTherapistRepository;
        this.notificationEventService = notificationEventService;
    }

    @Override
    @Transactional
    public AdminSessionResponse createSession(AdminSessionCreateRequest request) {

        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }

        Child child = childRepository.findById(request.getChildId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Child not found"));

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        Therapist therapist = therapistRepository.findById(request.getTherapistId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Therapist not found"));

        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(therapist.getUser().getActive())) {

            throw new IllegalArgumentException(
                    "Therapist account is inactive"
            );
        }

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Branch not found"));

        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new IllegalArgumentException(
                    "Branch is inactive"
            );
        }

        if (!child.getBranch().getId().equals(branch.getId())) {
            throw new IllegalArgumentException(
                    "Child does not belong to the selected branch"
            );
        }

        if (!therapist.getBranch().getId().equals(branch.getId())) {
            throw new IllegalArgumentException(
                    "Therapist does not belong to the selected branch"
            );
        }

        ChildTherapist assignment =
                childTherapistRepository
                        .findByChildIdAndTherapistIdAndActive(
                                child.getId(),
                                therapist.getId(),
                                true
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Therapist is not assigned to this child"
                                ));

        TherapySession session = new TherapySession();

        session.setChild(child);
        session.setTherapist(therapist);
        session.setBranch(branch);

        /*
         * Therapy type is taken from the therapist profile.
         * Admin does not manually enter it.
         */
        session.setTherapyType(therapist.getTherapyType());

        session.setSessionDate(request.getSessionDate());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setRemarks(request.getRemarks());

        /*
         * New session always starts as SCHEDULED.
         */
        session.setStatus(
                com.autism_rehablito_app.entity.SessionStatus.SCHEDULED
        );

        TherapySession savedSession =
                therapySessionRepository.save(session);
        
        notificationEventService.notifyUser(
                savedSession.getChild().getParent().getUser().getId(),
                savedSession.getBranch().getId(),
                "New Session Scheduled",
                "A new " + savedSession.getTherapyType()
                        + " session has been scheduled for "
                        + savedSession.getChild().getChildName() + ".",
                NotificationType.SESSION,
                "SESSION",
                savedSession.getId()
        );

        notificationEventService.notifyUser(
                savedSession.getTherapist().getUser().getId(),
                savedSession.getBranch().getId(),
                "New Session Assigned",
                "A new " + savedSession.getTherapyType()
                        + " session has been assigned to you for "
                        + savedSession.getChild().getChildName() + ".",
                NotificationType.SESSION,
                "SESSION",
                savedSession.getId()
        );

        return mapToResponse(savedSession);
    }

    @Override
    public List<AdminSessionResponse> getAllSessions() {

        return therapySessionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AdminSessionResponse> getSessionsByBranch(Long branchId) {

        branchRepository.findById(branchId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Branch not found"));

        return therapySessionRepository.findByBranchId(branchId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AdminSessionResponse getSessionById(Long sessionId) {

        TherapySession session =
                therapySessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Session not found"
                                ));

        return mapToResponse(session);
    }

    private AdminSessionResponse mapToResponse(
            TherapySession session) {

        return new AdminSessionResponse(
                session.getId(),

                session.getChild().getId(),
                session.getChild().getChildName(),

                session.getTherapist().getId(),
                session.getTherapist().getUser().getName(),
                session.getTherapyType(),

                session.getBranch().getId(),
                session.getBranch().getBranchName(),

                session.getSessionDate(),
                session.getStartTime(),
                session.getEndTime(),

                session.getStatus(),
                session.getRemarks(),

                session.getCreatedAt(),
                session.getUpdatedAt()
        );
    }
    
    @Override
    @Transactional
    public AdminSessionResponse updateSession(
            Long sessionId,
            AdminSessionUpdateRequest request) {

        TherapySession session =
                therapySessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Session not found"
                                ));

        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new IllegalArgumentException(
                    "Only scheduled sessions can be rescheduled"
            );
        }

        if (request.getStartTime() == null
                || request.getEndTime() == null) {

            throw new IllegalArgumentException(
                    "Start time and end time are required"
            );
        }

        if (!request.getStartTime()
                .isBefore(request.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }

        session.setSessionDate(
                request.getSessionDate()
        );

        session.setStartTime(
                request.getStartTime()
        );

        session.setEndTime(
                request.getEndTime()
        );

        session.setRemarks(
                request.getRemarks()
        );

        TherapySession updatedSession =
                therapySessionRepository.save(session);
        
        notificationEventService.notifyUser(
                session.getChild().getParent().getUser().getId(),
                session.getBranch().getId(),
                "Session Updated",
                "The " + session.getTherapyType()
                        + " session for "
                        + session.getChild().getChildName()
                        + " has been updated.",
                NotificationType.SESSION,
                "SESSION",
                session.getId()
        );

        notificationEventService.notifyUser(
                session.getTherapist().getUser().getId(),
                session.getBranch().getId(),
                "Session Updated",
                "The " + session.getTherapyType()
                        + " session for "
                        + session.getChild().getChildName()
                        + " has been updated.",
                NotificationType.SESSION,
                "SESSION",
                session.getId()
        );

        return mapToResponse(updatedSession);
    }
    
    @Override
    @Transactional
    public AdminSessionResponse cancelSession(
            Long sessionId) {

        TherapySession session =
                therapySessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Session not found"
                                ));

        if (session.getStatus() != SessionStatus.SCHEDULED) {
            throw new IllegalArgumentException(
                    "Only scheduled sessions can be cancelled"
            );
        }

        session.setStatus(SessionStatus.CANCELLED);

        TherapySession updatedSession =
                therapySessionRepository.save(session);
        
        notificationEventService.notifyUser(
                session.getChild().getParent().getUser().getId(),
                session.getBranch().getId(),
                "Session Cancelled",
                "The " + session.getTherapyType()
                        + " session for "
                        + session.getChild().getChildName()
                        + " has been cancelled.",
                NotificationType.SESSION,
                "SESSION",
                session.getId()
        );

        notificationEventService.notifyUser(
                session.getTherapist().getUser().getId(),
                session.getBranch().getId(),
                "Session Cancelled",
                "The " + session.getTherapyType()
                        + " session for "
                        + session.getChild().getChildName()
                        + " has been cancelled.",
                NotificationType.SESSION,
                "SESSION",
                session.getId()
        );

        return mapToResponse(updatedSession);
    }
}
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.therapist.TherapistSessionResponse;
import com.autism_rehablito_app.entity.SessionStatus;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.TherapySession;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.TherapySessionRepository;
import com.autism_rehablito_app.service.TherapistSessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.autism_rehablito_app.entity.NotificationType;
import com.autism_rehablito_app.service.NotificationEventService;

@Service
public class TherapistSessionServiceImpl
        implements TherapistSessionService {

    private final TherapySessionRepository therapySessionRepository;
    private final TherapistRepository therapistRepository;
    private final NotificationEventService notificationEventService;

    public TherapistSessionServiceImpl(
            TherapySessionRepository therapySessionRepository,
            TherapistRepository therapistRepository,
            NotificationEventService notificationEventService) {

        this.therapySessionRepository = therapySessionRepository;
        this.therapistRepository = therapistRepository;
        this.notificationEventService = notificationEventService;
    }

    @Override
    public List<TherapistSessionResponse> getMySessions(
            Long userId) {

        Therapist therapist = getActiveTherapist(userId);

        return therapySessionRepository
                .findByTherapistId(therapist.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TherapistSessionResponse getMySessionById(
            Long userId,
            Long sessionId) {

        Therapist therapist = getActiveTherapist(userId);

        TherapySession session =
                therapySessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Session not found"
                                ));

        validateSessionOwnership(session, therapist);

        return mapToResponse(session);
    }

    @Override
    @Transactional
    public TherapistSessionResponse startSession(
            Long userId,
            Long sessionId) {

        Therapist therapist = getActiveTherapist(userId);

        TherapySession session =
                therapySessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Session not found"
                                ));

        validateSessionOwnership(session, therapist);

        if (!Boolean.TRUE.equals(
                session.getChild().getActive())) {

            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        if (session.getStatus() != SessionStatus.SCHEDULED) {

            throw new IllegalArgumentException(
                    "Only scheduled sessions can be started"
            );
        }

        session.setStatus(SessionStatus.IN_PROGRESS);

        TherapySession updatedSession =
                therapySessionRepository.save(session);
        
        notificationEventService.notifyUser(
                session.getChild().getParent().getUser().getId(),
                session.getBranch().getId(),
                "Session Started",
                "The " + session.getTherapyType()
                        + " session for "
                        + session.getChild().getChildName()
                        + " has started.",
                NotificationType.SESSION,
                "SESSION",
                session.getId()
        );

        return mapToResponse(updatedSession);
    }

    @Override
    @Transactional
    public TherapistSessionResponse completeSession(
            Long userId,
            Long sessionId) {

        Therapist therapist = getActiveTherapist(userId);

        TherapySession session =
                therapySessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Session not found"
                                ));

        validateSessionOwnership(session, therapist);

        if (!Boolean.TRUE.equals(
                session.getChild().getActive())) {

            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        if (session.getStatus() != SessionStatus.IN_PROGRESS) {

            throw new IllegalArgumentException(
                    "Only in-progress sessions can be completed"
            );
        }

        session.setStatus(SessionStatus.COMPLETED);

        TherapySession updatedSession =
                therapySessionRepository.save(session);
        
        notificationEventService.notifyUser(
                session.getChild().getParent().getUser().getId(),
                session.getBranch().getId(),
                "Session Completed",
                "The " + session.getTherapyType()
                        + " session for "
                        + session.getChild().getChildName()
                        + " has been completed.",
                NotificationType.SESSION,
                "SESSION",
                session.getId()
        );

        return mapToResponse(updatedSession);
    }

    private Therapist getActiveTherapist(Long userId) {

        Therapist therapist =
                therapistRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Therapist profile not found"
                                ));

        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(
                therapist.getUser().getActive())) {

            throw new IllegalArgumentException(
                    "Therapist account is inactive"
            );
        }

        return therapist;
    }

    private void validateSessionOwnership(
            TherapySession session,
            Therapist therapist) {

        if (!session.getTherapist()
                .getId()
                .equals(therapist.getId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to access this session"
            );
        }
    }

    private TherapistSessionResponse mapToResponse(
            TherapySession session) {

        return new TherapistSessionResponse(
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
}
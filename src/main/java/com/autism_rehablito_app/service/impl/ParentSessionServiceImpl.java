package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.parent.ParentSessionResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.TherapySession;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.repository.TherapySessionRepository;
import com.autism_rehablito_app.service.ParentSessionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentSessionServiceImpl
        implements ParentSessionService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final TherapySessionRepository therapySessionRepository;

    public ParentSessionServiceImpl(
            ParentRepository parentRepository,
            ChildRepository childRepository,
            TherapySessionRepository therapySessionRepository) {

        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.therapySessionRepository =
                therapySessionRepository;
    }

    @Override
    public List<ParentSessionResponse> getMyChildSessions(
            Long userId,
            Long childId) {

        Parent parent =
                parentRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Parent profile not found"
                                ));

        Child child =
                childRepository.findById(childId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Child not found"
                                ));

        validateChildOwnership(parent, child);

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        return therapySessionRepository
                .findByChildId(childId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ParentSessionResponse getMyChildSessionById(
            Long userId,
            Long childId,
            Long sessionId) {

        Parent parent =
                parentRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Parent profile not found"
                                ));

        Child child =
                childRepository.findById(childId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Child not found"
                                ));

        validateChildOwnership(parent, child);

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        TherapySession session =
                therapySessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Session not found"
                                ));

        if (!session.getChild()
                .getId()
                .equals(childId)) {

            throw new IllegalArgumentException(
                    "Session does not belong to this child"
            );
        }

        return mapToResponse(session);
    }

    private void validateChildOwnership(
            Parent parent,
            Child child) {

        if (!child.getParent()
                .getId()
                .equals(parent.getId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to access this child"
            );
        }
    }

    private ParentSessionResponse mapToResponse(
            TherapySession session) {

        return new ParentSessionResponse(
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
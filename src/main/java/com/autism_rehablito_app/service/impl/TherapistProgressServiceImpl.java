
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.therapist.TherapistProgressRequest;
import com.autism_rehablito_app.dto.therapist.TherapistProgressResponse;
import com.autism_rehablito_app.entity.ProgressRecord;
import com.autism_rehablito_app.entity.SessionStatus;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.TherapySession;
import com.autism_rehablito_app.repository.ProgressRecordRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.TherapySessionRepository;
import com.autism_rehablito_app.service.TherapistProgressService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TherapistProgressServiceImpl
        implements TherapistProgressService {

    private final ProgressRecordRepository progressRecordRepository;
    private final TherapistRepository therapistRepository;
    private final TherapySessionRepository therapySessionRepository;

    public TherapistProgressServiceImpl(
            ProgressRecordRepository progressRecordRepository,
            TherapistRepository therapistRepository,
            TherapySessionRepository therapySessionRepository) {

        this.progressRecordRepository = progressRecordRepository;
        this.therapistRepository = therapistRepository;
        this.therapySessionRepository = therapySessionRepository;
    }

    @Override
    @Transactional
    public TherapistProgressResponse createProgress(
            Long userId,
            Long sessionId,
            TherapistProgressRequest request) {

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

        TherapySession session =
                therapySessionRepository.findById(sessionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Session not found"
                                ));

        if (!session.getTherapist()
                .getId()
                .equals(therapist.getId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to add progress to this session"
            );
        }

        if (!Boolean.TRUE.equals(
                session.getChild().getActive())) {

            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        if (session.getStatus() != SessionStatus.IN_PROGRESS
                && session.getStatus() != SessionStatus.COMPLETED) {

            throw new IllegalArgumentException(
                    "Progress can only be added to in-progress or completed sessions"
            );
        }

        if (progressRecordRepository.existsBySessionId(sessionId)) {

            throw new IllegalArgumentException(
                    "Progress has already been recorded for this session"
            );
        }

        ProgressRecord progressRecord = new ProgressRecord();

        progressRecord.setChild(session.getChild());
        progressRecord.setTherapist(therapist);
        progressRecord.setSession(session);
        progressRecord.setObservations(request.getObservations());
        progressRecord.setProgress(request.getProgress());
        progressRecord.setGoals(request.getGoals());
        progressRecord.setActivities(request.getActivities());
        progressRecord.setRemarks(request.getRemarks());

        ProgressRecord savedProgress =
                progressRecordRepository.save(progressRecord);

        return mapToResponse(savedProgress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TherapistProgressResponse> getChildProgress(
            Long userId,
            Long childId) {

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

        return progressRecordRepository
                .findByChildIdAndTherapistIdOrderByCreatedAtDesc(
                        childId,
                        therapist.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TherapistProgressResponse mapToResponse(
            ProgressRecord progressRecord) {

        TherapySession session = progressRecord.getSession();

        return new TherapistProgressResponse(
                progressRecord.getId(),

                progressRecord.getChild().getId(),
                progressRecord.getChild().getChildName(),

                progressRecord.getTherapist().getId(),
                progressRecord.getTherapist()
                        .getUser()
                        .getName(),

                session.getId(),

                session.getSessionDate(),
                session.getStartTime(),
                session.getEndTime(),

                progressRecord.getObservations(),
                progressRecord.getProgress(),
                progressRecord.getGoals(),
                progressRecord.getActivities(),
                progressRecord.getRemarks(),

                progressRecord.getCreatedAt(),
                progressRecord.getUpdatedAt()
        );
    }
}

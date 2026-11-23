
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.therapist.TherapistNoteRequest;
import com.autism_rehablito_app.dto.therapist.TherapistNoteResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.SessionStatus;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.TherapistNote;
import com.autism_rehablito_app.entity.TherapySession;
import com.autism_rehablito_app.repository.TherapistNoteRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.TherapySessionRepository;
import com.autism_rehablito_app.service.TherapistNoteService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TherapistNoteServiceImpl implements TherapistNoteService {

    private final TherapistNoteRepository therapistNoteRepository;
    private final TherapistRepository therapistRepository;
    private final TherapySessionRepository therapySessionRepository;

    public TherapistNoteServiceImpl(
            TherapistNoteRepository therapistNoteRepository,
            TherapistRepository therapistRepository,
            TherapySessionRepository therapySessionRepository) {

        this.therapistNoteRepository = therapistNoteRepository;
        this.therapistRepository = therapistRepository;
        this.therapySessionRepository = therapySessionRepository;
    }

    @Override
    @Transactional
    public TherapistNoteResponse createNote(
            Long userId,
            Long sessionId,
            TherapistNoteRequest request) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Therapist profile not found"));

        validateActiveTherapist(therapist);

        TherapySession session = therapySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Session not found"));

        if (session.getTherapist() == null
                || !session.getTherapist().getId().equals(therapist.getId())) {
            throw new IllegalArgumentException(
                    "You are not authorized to add a note to this session");
        }

        Child child = session.getChild();

        if (child == null || !Boolean.TRUE.equals(child.getActive())) {
            throw new IllegalArgumentException("Child account is inactive");
        }

        if (session.getStatus() != SessionStatus.IN_PROGRESS
                && session.getStatus() != SessionStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Notes can only be added to in-progress or completed sessions");
        }

        String noteText = request.getNote().trim();

        if (noteText.isEmpty()) {
            throw new IllegalArgumentException("Therapist note cannot be empty");
        }

        TherapistNote note = new TherapistNote();
        note.setChild(child);
        note.setTherapist(therapist);
        note.setSession(session);
        note.setNote(noteText);

        TherapistNote savedNote = therapistNoteRepository.save(note);

        return mapToResponse(savedNote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TherapistNoteResponse> getChildNotes(
            Long userId,
            Long childId) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Therapist profile not found"));

        validateActiveTherapist(therapist);

        return therapistNoteRepository
                .findByChildIdAndTherapistIdOrderByCreatedAtDesc(
                        childId,
                        therapist.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateActiveTherapist(Therapist therapist) {

        if (!Boolean.TRUE.equals(therapist.getActive())
                || therapist.getUser() == null
                || !Boolean.TRUE.equals(therapist.getUser().getActive())) {
            throw new IllegalArgumentException(
                    "Therapist account is inactive");
        }
    }

    private TherapistNoteResponse mapToResponse(TherapistNote note) {

        return new TherapistNoteResponse(
                note.getId(),
                note.getChild().getId(),
                note.getChild().getChildName(),
                note.getTherapist().getId(),
                note.getTherapist().getUser().getName(),
                note.getSession().getId(),
                note.getNote(),
                note.getCreatedAt());
    }
}

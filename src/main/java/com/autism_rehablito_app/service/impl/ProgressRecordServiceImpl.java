
package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.ProgressRecordResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.entity.ProgressRecord;
import com.autism_rehablito_app.entity.TherapySession;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.repository.ProgressRecordRepository;
import com.autism_rehablito_app.service.ProgressRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProgressRecordServiceImpl implements ProgressRecordService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final ProgressRecordRepository progressRecordRepository;

    public ProgressRecordServiceImpl(
            ParentRepository parentRepository,
            ChildRepository childRepository,
            ProgressRecordRepository progressRecordRepository) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.progressRecordRepository = progressRecordRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressRecordResponse> getMyChildProgress(
            Long userId,
            Long childId) {

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Parent profile not found"));

        if (!Boolean.TRUE.equals(parent.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Parent account is inactive");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Child not found"));

        if (!Boolean.TRUE.equals(child.getActive())
                || !child.getParent().getId().equals(parent.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not authorized to view this child's progress");
        }

        return progressRecordRepository
                .findByChildIdOrderByCreatedAtDesc(childId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProgressRecordResponse mapToResponse(ProgressRecord record) {

        Child child = record.getChild();
        TherapySession session = record.getSession();
        Therapist therapist = record.getTherapist();

        ProgressRecordResponse response = new ProgressRecordResponse();

        response.setId(record.getId());
        response.setChildId(child.getId());
        response.setChildName(child.getChildName());

        response.setSessionId(session.getId());
        response.setSessionType(session.getTherapyType());

        if (session.getSessionDate() != null) {
            response.setSessionDate(
                    session.getSessionDate().atStartOfDay());
        }

        response.setTherapistId(therapist.getId());
        response.setTherapistName(therapist.getUser().getName());

        response.setObservations(record.getObservations());
        response.setProgress(record.getProgress());
        response.setGoals(record.getGoals());
        response.setActivities(record.getActivities());
        response.setRemarks(record.getRemarks());
        response.setCreatedAt(record.getCreatedAt());

        return response;
    }
}

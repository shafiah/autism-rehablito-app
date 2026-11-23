package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.therapist.TherapistHomeworkCreateRequest;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkRemarksRequest;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkResponse;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkStatusUpdateRequest;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkUpdateRequest;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapist;
import com.autism_rehablito_app.entity.Homework;
import com.autism_rehablito_app.entity.HomeworkStatus;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.entity.TherapySession;
import com.autism_rehablito_app.exception.BadRequestException;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ChildTherapistRepository;
import com.autism_rehablito_app.repository.HomeworkRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.repository.TherapySessionRepository;
import com.autism_rehablito_app.service.TherapistHomeworkService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.autism_rehablito_app.entity.NotificationType;
import com.autism_rehablito_app.service.NotificationEventService;

@Service
@Transactional
public class TherapistHomeworkServiceImpl
        implements TherapistHomeworkService {

    private final TherapistRepository therapistRepository;
    private final ChildRepository childRepository;
    private final ChildTherapistRepository childTherapistRepository;
    private final TherapySessionRepository therapySessionRepository;
    private final HomeworkRepository homeworkRepository;
    private final NotificationEventService notificationEventService;

    public TherapistHomeworkServiceImpl(
            TherapistRepository therapistRepository,
            ChildRepository childRepository,
            ChildTherapistRepository childTherapistRepository,
            TherapySessionRepository therapySessionRepository,
            HomeworkRepository homeworkRepository,
            NotificationEventService notificationEventService) {

        this.therapistRepository = therapistRepository;
        this.childRepository = childRepository;
        this.childTherapistRepository = childTherapistRepository;
        this.therapySessionRepository = therapySessionRepository;
        this.homeworkRepository = homeworkRepository;
        this.notificationEventService = notificationEventService;
    }

    @Override
    public TherapistHomeworkResponse createHomework(
            Long userId,
            Long childId,
            TherapistHomeworkCreateRequest request) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist not found"));

        if (!therapist.getActive()) {
            throw new BadRequestException(
                    "Therapist account is inactive");
        }

        if (!therapist.getUser().getActive()) {
            throw new BadRequestException(
                    "Therapist user account is inactive");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException("Child not found"));

        if (!child.getActive()) {
            throw new BadRequestException(
                    "Child account is inactive");
        }

        ChildTherapist assignment =
                childTherapistRepository
                        .findByChildIdAndTherapistIdAndActive(
                                childId,
                                therapist.getId(),
                                true)
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "Therapist is not assigned to this child"));
        
        String requestedTherapyType = request.getTherapyType();

        if (requestedTherapyType == null
                || requestedTherapyType.isBlank()) {
            throw new BadRequestException("Therapy type is required");
        }

        Set<String> allowedTherapyTypes = Set.of(
                "Occupational Therapy",
                "Speech Therapy"
        );

        String normalizedTherapyType = requestedTherapyType.trim();

        String therapyType = allowedTherapyTypes.stream()
                .filter(type -> type.equalsIgnoreCase(normalizedTherapyType))
                .findFirst()
                .orElseThrow(() ->
                        new BadRequestException(
                                "Invalid therapy type. Allowed values: "
                                        + "Occupational Therapy, Speech Therapy"));

        LocalDate assignedDate = LocalDate.now();

        if (request.getDueDate().isBefore(assignedDate)) {
            throw new BadRequestException(
                    "Due date cannot be before assigned date");
        }

        TherapySession session = null;

        if (request.getSessionId() != null) {

            session = therapySessionRepository
                    .findById(request.getSessionId())
                    .orElseThrow(() ->
                            new BadRequestException(
                                    "Session not found"));

            if (!session.getChild().getId().equals(childId)) {
                throw new BadRequestException(
                        "Session does not belong to this child");
            }

            if (!session.getTherapist().getId()
                    .equals(therapist.getId())) {

                throw new BadRequestException(
                        "Session does not belong to this therapist");
            }
        }

        Homework homework = new Homework();

        homework.setChild(child);
        homework.setTherapist(therapist);
        homework.setSession(session);
        homework.setTherapyType(therapyType);
        homework.setTitle(request.getTitle());
        homework.setInstructions(request.getInstructions());
        homework.setAssignedDate(assignedDate);
        homework.setDueDate(request.getDueDate());
        homework.setStatus(HomeworkStatus.ASSIGNED);

        Homework savedHomework =
                homeworkRepository.save(homework);
        
        notificationEventService.notifyUser(
                child.getParent().getUser().getId(),
                child.getBranch().getId(),
                "New Homework Assigned",
                "New homework has been assigned for "
                        + child.getChildName()
                        + ".",
                NotificationType.HOMEWORK,
                "HOMEWORK",
                savedHomework.getId()
        );

        return mapToResponse(savedHomework);
    }

    private TherapistHomeworkResponse mapToResponse(
            Homework homework) {

        TherapistHomeworkResponse response =
                new TherapistHomeworkResponse(
                        homework.getId(),
                        homework.getChild().getId(),
                        homework.getChild().getChildName(),
                        homework.getTherapist().getId(),
                        homework.getTherapist().getUser().getName(),
                        homework.getSession() != null
                                ? homework.getSession().getId()
                                : null,
                        homework.getTitle(),
                        homework.getInstructions(),
                        homework.getAssignedDate(),
                        homework.getDueDate(),
                        homework.getStatus(),
                        homework.getParentRemarks(),
                        homework.getCreatedAt(),
                        homework.getUpdatedAt()
                );

        response.setParentName(
                homework.getChild()
                        .getParent()
                        .getUser()
                        .getName()
        );

        // New code: map therapist remarks separately
        response.setTherapistRemarks(
                homework.getTherapistRemarks()
        );

        return response;
    }    @Override
    @Transactional(readOnly = true)
    public List<TherapistHomeworkResponse> getMyHomework(
            Long userId) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist not found"));

        if (!therapist.getActive()) {
            throw new BadRequestException(
                    "Therapist account is inactive");
        }

        if (!therapist.getUser().getActive()) {
            throw new BadRequestException(
                    "Therapist user account is inactive");
        }

        List<Homework> homeworkList =
                homeworkRepository.findByTherapistId(
                        therapist.getId());

        return homeworkList.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public TherapistHomeworkResponse getMyHomeworkById(
            Long userId,
            Long homeworkId) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist not found"));

        if (!therapist.getActive()) {
            throw new BadRequestException(
                    "Therapist account is inactive");
        }

        if (!therapist.getUser().getActive()) {
            throw new BadRequestException(
                    "Therapist user account is inactive");
        }

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() ->
                        new BadRequestException("Homework not found"));

        if (!homework.getTherapist().getId()
                .equals(therapist.getId())) {

            throw new BadRequestException(
                    "You are not authorized to access this homework");
        }

        return mapToResponse(homework);
    }
    
    @Override
    @Transactional
    public TherapistHomeworkResponse updateHomeworkStatus(
            Long userId,
            Long homeworkId,
            TherapistHomeworkStatusUpdateRequest request) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist not found"));

        if (!therapist.getActive()) {
            throw new BadRequestException(
                    "Therapist account is inactive");
        }

        if (!therapist.getUser().getActive()) {
            throw new BadRequestException(
                    "Therapist user account is inactive");
        }

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() ->
                        new BadRequestException("Homework not found"));

        if (!homework.getTherapist().getId()
                .equals(therapist.getId())) {

            throw new BadRequestException(
                    "You are not authorized to update this homework");
        }

        HomeworkStatus currentStatus = homework.getStatus();
        HomeworkStatus requestedStatus = request.getStatus();

        if (currentStatus == HomeworkStatus.ASSIGNED) {

            if (requestedStatus != HomeworkStatus.IN_PROGRESS) {
                throw new BadRequestException(
                        "ASSIGNED homework can only move to IN_PROGRESS");
            }

        } else if (currentStatus == HomeworkStatus.IN_PROGRESS) {

            if (requestedStatus != HomeworkStatus.COMPLETED) {
                throw new BadRequestException(
                        "IN_PROGRESS homework can only move to COMPLETED");
            }

        } else if (currentStatus == HomeworkStatus.COMPLETED) {

            throw new BadRequestException(
                    "Completed homework cannot be updated");
        }

        homework.setStatus(requestedStatus);

        Homework savedHomework =
                homeworkRepository.save(homework);

        return mapToResponse(savedHomework);
    }
    
    @Override
    public TherapistHomeworkResponse updateHomework(
            Long userId,
            Long homeworkId,
            TherapistHomeworkUpdateRequest request) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist not found"));

        if (!therapist.getActive()
                || !therapist.getUser().getActive()) {
            throw new BadRequestException(
                    "Therapist account is inactive");
        }

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() ->
                        new BadRequestException("Homework not found"));

        if (!homework.getTherapist().getId()
                .equals(therapist.getId())) {
            throw new BadRequestException(
                    "You are not authorized to update this homework");
        }

        if (homework.getStatus() == HomeworkStatus.COMPLETED) {
            throw new BadRequestException(
                    "Completed homework cannot be updated");
        }

        if (request.getDueDate().isBefore(LocalDate.now())) {
            throw new BadRequestException(
                    "Due date cannot be in the past");
        }

        homework.setTitle(request.getTitle().trim());
        homework.setInstructions(request.getInstructions().trim());
        homework.setDueDate(request.getDueDate());

        Homework savedHomework = homeworkRepository.save(homework);

        return mapToResponse(savedHomework);
    }
    
    @Override
    @Transactional
    public TherapistHomeworkResponse updateTherapistRemarks(
            Long userId,
            Long homeworkId,
            TherapistHomeworkRemarksRequest request) {

        // 1. Find the authenticated therapist
        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist not found"));

        // 2. Verify therapist account is active
        if (!Boolean.TRUE.equals(therapist.getActive())
                || !Boolean.TRUE.equals(therapist.getUser().getActive())) {
            throw new BadRequestException("Therapist account is inactive");
        }

        // 3. Find the homework
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() ->
                        new BadRequestException("Homework not found"));

        // 4. Ensure this homework belongs to the authenticated therapist
        if (!homework.getTherapist().getId().equals(therapist.getId())) {
            throw new BadRequestException(
                    "You are not authorized to update remarks for this homework");
        }

        // 5. Validate and save therapist remarks separately
        if (request.getTherapistRemarks() == null
                || request.getTherapistRemarks().trim().isEmpty()) {
            throw new BadRequestException("Therapist remarks are required");
        }

        homework.setTherapistRemarks(
                request.getTherapistRemarks().trim());

        Homework savedHomework = homeworkRepository.save(homework);

        // 6. Return the updated homework response
        return mapToResponse(savedHomework);
    }

    @Override
    public void deleteHomework(Long userId, Long homeworkId) {

        Therapist therapist = therapistRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException("Therapist not found"));

        if (!therapist.getActive()
                || !therapist.getUser().getActive()) {
            throw new BadRequestException(
                    "Therapist account is inactive");
        }

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() ->
                        new BadRequestException("Homework not found"));

        if (!homework.getTherapist().getId()
                .equals(therapist.getId())) {
            throw new BadRequestException(
                    "You are not authorized to delete this homework");
        }

        if (homework.getStatus() == HomeworkStatus.COMPLETED) {
            throw new BadRequestException(
                    "Completed homework cannot be deleted");
        }

        homeworkRepository.delete(homework);
    }
}
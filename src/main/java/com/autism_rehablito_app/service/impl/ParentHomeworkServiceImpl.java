package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.parent.ParentHomeworkRemarkRequest;
import com.autism_rehablito_app.dto.parent.ParentHomeworkResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.Homework;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.exception.BadRequestException;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.HomeworkRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.service.ParentHomeworkService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ParentHomeworkServiceImpl
        implements ParentHomeworkService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final HomeworkRepository homeworkRepository;

    public ParentHomeworkServiceImpl(
            ParentRepository parentRepository,
            ChildRepository childRepository,
            HomeworkRepository homeworkRepository) {

        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.homeworkRepository = homeworkRepository;
    }

    @Override
    public List<ParentHomeworkResponse> getMyChildHomework(
            Long userId,
            Long childId) {

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Parent profile not found"));

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Child not found"));

        validateParentChildAccess(parent, child);

        List<Homework> homeworkList =
                homeworkRepository.findByChildId(childId);

        return homeworkList.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ParentHomeworkResponse getMyChildHomeworkById(
            Long userId,
            Long childId,
            Long homeworkId) {

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Parent profile not found"));

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Child not found"));

        validateParentChildAccess(parent, child);

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Homework not found"));

        if (!homework.getChild().getId().equals(childId)) {
            throw new BadRequestException(
                    "Homework does not belong to this child");
        }

        return mapToResponse(homework);
    }

    private void validateParentChildAccess(
            Parent parent,
            Child child) {

        if (!child.getParent().getId().equals(parent.getId())) {
            throw new BadRequestException(
                    "You are not authorized to access this child");
        }

        if (!child.getActive()) {
            throw new BadRequestException(
                    "Child account is inactive");
        }
    }

    private ParentHomeworkResponse mapToResponse(
            Homework homework) {


return new ParentHomeworkResponse(
        homework.getId(),
        homework.getChild().getId(),
        homework.getChild().getChildName(),
        homework.getTherapist().getId(),
        homework.getTherapist().getUser().getName(),
        homework.getSession() != null
                ? homework.getSession().getId()
                : null,
        homework.getTherapyType(),
        homework.getTitle(),
        homework.getInstructions(),
        homework.getAssignedDate(),
        homework.getDueDate(),
        homework.getStatus(),
        homework.getParentRemarks(),
        homework.getTherapistRemarks(),
        homework.getCreatedAt(),
        homework.getUpdatedAt()
);

    }
    
    @Override
    @Transactional
    public ParentHomeworkResponse addOrUpdateRemark(
            Long userId,
            Long childId,
            Long homeworkId,
            ParentHomeworkRemarkRequest request) {

        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Parent profile not found"));

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Child not found"));

        validateParentChildAccess(parent, child);

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Homework not found"));

        if (!homework.getChild().getId().equals(childId)) {
            throw new BadRequestException(
                    "Homework does not belong to this child");
        }

        homework.setParentRemarks(
                request.getParentRemarks().trim()
        );

        Homework savedHomework =
                homeworkRepository.save(homework);

        return mapToResponse(savedHomework);
    }
}
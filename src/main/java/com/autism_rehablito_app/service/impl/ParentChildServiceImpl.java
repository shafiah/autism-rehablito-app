package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.parent.ParentChildResponse;
import com.autism_rehablito_app.dto.parent.ParentChildTherapistResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapist;
import com.autism_rehablito_app.entity.Parent;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ChildTherapistRepository;
import com.autism_rehablito_app.repository.ParentRepository;
import com.autism_rehablito_app.service.ParentChildService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParentChildServiceImpl
        implements ParentChildService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final ChildTherapistRepository childTherapistRepository;

    public ParentChildServiceImpl(
            ParentRepository parentRepository,
            ChildRepository childRepository,
            ChildTherapistRepository childTherapistRepository) {

        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.childTherapistRepository = childTherapistRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParentChildResponse> getMyChildren(
            Long userId) {

        Parent parent = parentRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Parent profile not found"
                        )
                );

        return childRepository
                .findByParentId(parent.getId())
                .stream()
                .filter(child ->
                        Boolean.TRUE.equals(child.getActive())
                )
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ParentChildResponse getMyChildById(
            Long userId,
            Long childId) {

        Parent parent = parentRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Parent profile not found"
                        )
                );

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Child not found with id: "
                                        + childId
                        )
                );

        if (!child.getParent().getId()
                .equals(parent.getId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to access this child"
            );
        }

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        return mapToResponse(child);
    }

    private ParentChildResponse mapToResponse(
            Child child) {

        return new ParentChildResponse(
                child.getId(),
                child.getChildName(),
                child.getDateOfBirth(),
                child.getGender(),
                child.getBranch().getId(),
                child.getBranch().getBranchName(),
                child.getMedicalNotes(),
                child.getActive(),
                child.getCreatedAt(),
                child.getImagePath()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ParentChildTherapistResponse> getMyChildTherapists(
            Long userId,
            Long childId) {

        Parent parent = parentRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Parent profile not found"
                        )
                );

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Child not found with id: " + childId
                        )
                );

        if (!child.getParent().getId()
                .equals(parent.getId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to access this child"
            );
        }

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        return childTherapistRepository
                .findByChildIdAndActive(childId, true)
                .stream()
                .map(this::mapToTherapistResponse)
                .toList();
    }
    
    private ParentChildTherapistResponse mapToTherapistResponse(
            ChildTherapist assignment) {

        return new ParentChildTherapistResponse(
                assignment.getId(),

                assignment.getTherapist().getId(),

                assignment.getTherapist()
                        .getUser()
                        .getName(),

                assignment.getTherapist()
                        .getTherapyType(),

                assignment.getTherapist()
                        .getBranch()
                        .getId(),

                assignment.getTherapist()
                        .getBranch()
                        .getBranchName(),

                assignment.getActive(),

                assignment.getAssignedAt()
        );
    }
}
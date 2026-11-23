package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.therapist.TherapistChildResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapist;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.repository.ChildTherapistRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.service.TherapistChildService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TherapistChildServiceImpl
        implements TherapistChildService {

    private final TherapistRepository therapistRepository;
    private final ChildTherapistRepository childTherapistRepository;

    public TherapistChildServiceImpl(
            TherapistRepository therapistRepository,
            ChildTherapistRepository childTherapistRepository) {

        this.therapistRepository = therapistRepository;
        this.childTherapistRepository =
                childTherapistRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TherapistChildResponse> getMyChildren(
            Long userId) {

        Therapist therapist = getTherapistByUserId(userId);

        return childTherapistRepository
                .findByTherapistId(therapist.getId())
                .stream()
                .filter(assignment ->
                        Boolean.TRUE.equals(
                                assignment.getActive()
                        )
                )
                .map(ChildTherapist::getChild)
                .filter(child ->
                        Boolean.TRUE.equals(
                                child.getActive()
                        )
                )
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TherapistChildResponse getMyChildById(
            Long userId,
            Long childId) {

        Therapist therapist = getTherapistByUserId(userId);

        ChildTherapist assignment =
                childTherapistRepository
                        .findByChildIdAndTherapistIdAndActive(
                                childId,
                                therapist.getId(),
                                true
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "This child is not assigned to you"
                                )
                        );

        Child child = assignment.getChild();

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new IllegalArgumentException(
                    "Child account is inactive"
            );
        }

        return mapToResponse(child);
    }

    private Therapist getTherapistByUserId(
            Long userId) {

        Therapist therapist =
                therapistRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Therapist profile not found"
                                )
                        );

        if (!Boolean.TRUE.equals(therapist.getActive())) {
            throw new IllegalArgumentException(
                    "Therapist account is inactive"
            );
        }

        if (!Boolean.TRUE.equals(
                therapist.getUser().getActive())) {

            throw new IllegalArgumentException(
                    "Therapist user account is inactive"
            );
        }

        return therapist;
    }


private TherapistChildResponse mapToResponse(
        Child child) {

    String parentName = null;

    if (child.getParent() != null
            && child.getParent().getUser() != null) {

        parentName = child.getParent()
                .getUser()
                .getName();
    }

    return new TherapistChildResponse(
            child.getId(),
            child.getChildName(),
            child.getDateOfBirth(),
            child.getGender(),
            parentName,
            child.getBranch().getId(),
            child.getBranch().getBranchName(),
            child.getMedicalNotes(),
            child.getActive(),
            child.getCreatedAt()
    );
}

}
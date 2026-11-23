package com.autism_rehablito_app.service.impl;

import com.autism_rehablito_app.dto.admin.AdminChildTherapistResponse;
import com.autism_rehablito_app.entity.Child;
import com.autism_rehablito_app.entity.ChildTherapist;
import com.autism_rehablito_app.entity.Therapist;
import com.autism_rehablito_app.repository.ChildRepository;
import com.autism_rehablito_app.repository.ChildTherapistRepository;
import com.autism_rehablito_app.repository.TherapistRepository;
import com.autism_rehablito_app.service.AdminChildTherapistService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminChildTherapistServiceImpl
        implements AdminChildTherapistService {

    private final ChildRepository childRepository;
    private final TherapistRepository therapistRepository;
    private final ChildTherapistRepository childTherapistRepository;

    public AdminChildTherapistServiceImpl(
            ChildRepository childRepository,
            TherapistRepository therapistRepository,
            ChildTherapistRepository childTherapistRepository) {

        this.childRepository = childRepository;
        this.therapistRepository = therapistRepository;
        this.childTherapistRepository = childTherapistRepository;
    }

    @Override
    @Transactional
    public AdminChildTherapistResponse assignTherapist(
            Long childId,
            Long therapistId) {

        Child child = childRepository.findById(childId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Child not found with id: " + childId
                        )
                );

        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Therapist not found with id: "
                                        + therapistId
                        )
                );

        if (!Boolean.TRUE.equals(child.getActive())) {
            throw new IllegalArgumentException(
                    "Cannot assign therapist to an inactive child"
            );
        }

        if (!Boolean.TRUE.equals(therapist.getActive())) {
            throw new IllegalArgumentException(
                    "Cannot assign an inactive therapist"
            );
        }

        if (!Boolean.TRUE.equals(therapist.getUser().getActive())) {
            throw new IllegalArgumentException(
                    "Therapist user account is inactive"
            );
        }

        if (!therapist.getBranch().getId()
                .equals(child.getBranch().getId())) {

            throw new IllegalArgumentException(
                    "Child and therapist must belong to the same branch"
            );
        }

        Optional<ChildTherapist> existingAssignment =
                childTherapistRepository
                        .findByChildIdAndTherapistId(
                                childId,
                                therapistId
                        );

        if (existingAssignment.isPresent()) {

            ChildTherapist assignment =
                    existingAssignment.get();

            if (Boolean.TRUE.equals(assignment.getActive())) {
                throw new IllegalArgumentException(
                        "Therapist is already assigned to this child"
                );
            }

            assignment.setActive(true);
            assignment.setUnassignedAt(null);

            ChildTherapist reactivatedAssignment =
                    childTherapistRepository.save(assignment);

            return mapToResponse(reactivatedAssignment);
        }

        ChildTherapist assignment = new ChildTherapist();

        assignment.setChild(child);
        assignment.setTherapist(therapist);
        assignment.setActive(true);

        ChildTherapist savedAssignment =
                childTherapistRepository.save(assignment);

        return mapToResponse(savedAssignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminChildTherapistResponse> getTherapistsByChild(
            Long childId) {

        childRepository.findById(childId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Child not found with id: " + childId
                        )
                );

        return childTherapistRepository
                .findByChildId(childId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminChildTherapistResponse> getChildrenByTherapist(
            Long therapistId) {

        therapistRepository.findById(therapistId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Therapist not found with id: "
                                        + therapistId
                        )
                );

        return childTherapistRepository
                .findByTherapistId(therapistId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public AdminChildTherapistResponse unassignTherapist(
            Long assignmentId) {

        ChildTherapist assignment =
                childTherapistRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Assignment not found with id: "
                                                + assignmentId
                                )
                        );

        if (!Boolean.TRUE.equals(assignment.getActive())) {
            throw new IllegalArgumentException(
                    "Therapist is already unassigned from this child"
            );
        }

        assignment.setActive(false);
        assignment.setUnassignedAt(LocalDateTime.now());

        ChildTherapist updatedAssignment =
                childTherapistRepository.save(assignment);

        return mapToResponse(updatedAssignment);
    }

    private AdminChildTherapistResponse mapToResponse(
            ChildTherapist assignment) {

        Child child = assignment.getChild();
        Therapist therapist = assignment.getTherapist();

        return new AdminChildTherapistResponse(
                assignment.getId(),

                child.getId(),
                child.getChildName(),

                therapist.getId(),
                therapist.getUser().getName(),
                therapist.getUser().getEmail(),
                therapist.getTherapyType(),

                therapist.getBranch().getId(),
                therapist.getBranch().getBranchName(),

                assignment.getActive(),
                assignment.getAssignedAt(),
                assignment.getUnassignedAt()
        );
    }
}
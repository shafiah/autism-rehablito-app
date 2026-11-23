package com.autism_rehablito_app.service.impl;


import com.autism_rehablito_app.dto.admin.AdminHomeworkResponse;
import com.autism_rehablito_app.entity.Homework;
import com.autism_rehablito_app.entity.HomeworkStatus;
import com.autism_rehablito_app.repository.HomeworkRepository;
import com.autism_rehablito_app.service.AdminHomeworkService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class AdminHomeworkServiceImpl implements AdminHomeworkService {

    private final HomeworkRepository homeworkRepository;

    public AdminHomeworkServiceImpl(
            HomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
    }

    @Override
    public List<AdminHomeworkResponse> getHomework(
            Long branchId,
            Long childId,
            String status,
            String therapyType) {

        HomeworkStatus homeworkStatus = parseStatus(status);

        String normalizedTherapyType =
                (therapyType == null
                        || therapyType.isBlank()
                        || therapyType.equalsIgnoreCase("All Therapies")
                        || therapyType.equalsIgnoreCase("All Therapy Types"))
                        ? null
                        : therapyType.trim();
        
        List<Homework> homeworkList =
                homeworkRepository.findAdminHomework(
                        branchId,
                        childId,
                        homeworkStatus,
                        normalizedTherapyType
                );

        return homeworkList.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AdminHomeworkResponse getHomeworkById(Long homeworkId) {

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Homework not found with ID: " + homeworkId
                        ));

        return mapToResponse(homework);
    }

    private HomeworkStatus parseStatus(String status) {

        if (status == null
                || status.isBlank()
                || status.equalsIgnoreCase("All Status")) {
            return null;
        }

        String normalizedStatus = status.trim()
                .replace(" ", "_")
                .toUpperCase(Locale.ROOT);

        try {
            return HomeworkStatus.valueOf(normalizedStatus);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid homework status: " + status
            );
        }
    }

    private AdminHomeworkResponse mapToResponse(Homework homework) {

        AdminHomeworkResponse response = new AdminHomeworkResponse();

        response.setHomeworkId(homework.getId());

        // Child details
        if (homework.getChild() != null) {
            response.setChildId(homework.getChild().getId());
            response.setChildName(homework.getChild().getChildName());

            // Branch is obtained through the child's assigned branch.
            if (homework.getChild().getBranch() != null) {
                response.setBranchId(
                        homework.getChild().getBranch().getId()
                );
                response.setBranchName(
                        homework.getChild().getBranch().getBranchName()
                );
            }

            // Parent details
            if (homework.getChild().getParent() != null
                    && homework.getChild().getParent().getUser() != null) {
                response.setParentName(
                        homework.getChild().getParent()
                                .getUser().getName()
                );
            }
        }

        // Therapist details
        if (homework.getTherapist() != null) {
            response.setTherapistId(homework.getTherapist().getId());

            if (homework.getTherapist().getUser() != null) {
                response.setTherapistName(
                        homework.getTherapist()
                                .getUser().getName()
                );
            }
        }

     // Optional therapy session details
        if (homework.getSession() != null) {
            response.setSessionId(homework.getSession().getId());
        }

        // Therapy type is stored directly with Homework.
        response.setTherapyType(homework.getTherapyType());

        // Homework details
        response.setTitle(homework.getTitle());
        response.setInstructions(homework.getInstructions());
        response.setAssignedDate(homework.getAssignedDate());
        response.setDueDate(homework.getDueDate());

        if (homework.getStatus() != null) {
            response.setStatus(homework.getStatus().name());
        }

        response.setParentRemarks(homework.getParentRemarks());
        
        response.setTherapistRemarks(homework.getTherapistRemarks());

        // Homework currently has no parentInstructions field.
        // Leave this DTO field null rather than inventing a value.
        response.setParentInstructions(null);

        return response;
    }
}

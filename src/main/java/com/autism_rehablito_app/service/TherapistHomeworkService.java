package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.therapist.TherapistHomeworkCreateRequest;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkRemarksRequest;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkResponse;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkStatusUpdateRequest;
import com.autism_rehablito_app.dto.therapist.TherapistHomeworkUpdateRequest;

import java.util.List;

public interface TherapistHomeworkService {

    TherapistHomeworkResponse createHomework(
            Long userId,
            Long childId,
            TherapistHomeworkCreateRequest request
    );

    List<TherapistHomeworkResponse> getMyHomework(
            Long userId
    );

    TherapistHomeworkResponse getMyHomeworkById(
            Long userId,
            Long homeworkId
    );

    TherapistHomeworkResponse updateHomeworkStatus(
            Long userId,
            Long homeworkId,
            TherapistHomeworkStatusUpdateRequest request
    );
    
    TherapistHomeworkResponse updateHomework(
            Long userId,
            Long homeworkId,
            TherapistHomeworkUpdateRequest request
    );

    void deleteHomework(
            Long userId,
            Long homeworkId
    );
    
    TherapistHomeworkResponse updateTherapistRemarks(
            Long userId,
            Long homeworkId,
            TherapistHomeworkRemarksRequest request
    );
}
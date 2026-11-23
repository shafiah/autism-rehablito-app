package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.parent.ParentHomeworkRemarkRequest;
import com.autism_rehablito_app.dto.parent.ParentHomeworkResponse;

import java.util.List;

public interface ParentHomeworkService {

    List<ParentHomeworkResponse> getMyChildHomework(
            Long userId,
            Long childId
    );

    ParentHomeworkResponse getMyChildHomeworkById(
            Long userId,
            Long childId,
            Long homeworkId
    );

    ParentHomeworkResponse addOrUpdateRemark(
            Long userId,
            Long childId,
            Long homeworkId,
            ParentHomeworkRemarkRequest request
    );
}
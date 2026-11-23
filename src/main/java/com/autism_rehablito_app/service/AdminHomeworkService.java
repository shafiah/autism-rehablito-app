
package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.admin.AdminHomeworkResponse;

import java.util.List;

public interface AdminHomeworkService {

    List<AdminHomeworkResponse> getHomework(
            Long branchId,
            Long childId,
            String status,
            String therapyType
    );

    AdminHomeworkResponse getHomeworkById(Long homeworkId);
}

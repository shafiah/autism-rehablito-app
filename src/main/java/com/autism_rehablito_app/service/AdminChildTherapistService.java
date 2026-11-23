package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.admin.AdminChildTherapistResponse;

import java.util.List;

public interface AdminChildTherapistService {

    AdminChildTherapistResponse assignTherapist(
            Long childId,
            Long therapistId
    );

    List<AdminChildTherapistResponse> getTherapistsByChild(
            Long childId
    );

    List<AdminChildTherapistResponse> getChildrenByTherapist(
            Long therapistId
    );

    AdminChildTherapistResponse unassignTherapist(
            Long assignmentId
    );
}
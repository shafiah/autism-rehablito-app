package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.admin.AdminTherapistCreateRequest;
import com.autism_rehablito_app.dto.admin.AdminTherapistCreateResponse;
import com.autism_rehablito_app.dto.admin.AdminTherapistResponse;

import java.util.List;

public interface AdminTherapistService {

    AdminTherapistCreateResponse createTherapist(
            AdminTherapistCreateRequest request
    );

    List<AdminTherapistResponse> getAllTherapists();

    List<AdminTherapistResponse> getTherapistsByBranch(Long branchId);

    AdminTherapistResponse getTherapistById(Long therapistId);

    AdminTherapistResponse activateTherapist(Long therapistId);

    AdminTherapistResponse deactivateTherapist(Long therapistId);
}
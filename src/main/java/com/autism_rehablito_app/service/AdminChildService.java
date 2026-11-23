package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.admin.AdminChildCreateRequest;
import com.autism_rehablito_app.dto.admin.AdminChildResponse;
import com.autism_rehablito_app.dto.admin.AdminChildUpdateRequest;
import com.autism_rehablito_app.dto.admin.AdminParentOptionResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;

public interface AdminChildService {

    AdminChildResponse createChild(AdminChildCreateRequest request);

    List<AdminChildResponse> getAllChildren();

    List<AdminChildResponse> getChildrenByBranch(Long branchId);

    AdminChildResponse getChildById(Long childId);
    
    AdminChildResponse updateChild(
            Long childId,
            AdminChildUpdateRequest request
    );

    AdminChildResponse activateChild(Long childId);

    AdminChildResponse deactivateChild(Long childId);
    
    List<AdminParentOptionResponse> getParentsByBranch(Long branchId);
    
    AdminChildResponse uploadChildImage(
            Long childId,
            MultipartFile image
    );
    Resource getChildImage(Long childId);
    
}
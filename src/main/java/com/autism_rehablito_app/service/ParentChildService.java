package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.parent.ParentChildResponse;
import com.autism_rehablito_app.dto.parent.ParentChildTherapistResponse;

import java.util.List;

public interface ParentChildService {

    List<ParentChildResponse> getMyChildren(Long userId);

    ParentChildResponse getMyChildById(
            Long userId,
            Long childId
    );
    
    List<ParentChildTherapistResponse> getMyChildTherapists(
            Long userId,
            Long childId
    );
}
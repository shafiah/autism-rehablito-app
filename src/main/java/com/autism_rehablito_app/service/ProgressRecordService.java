
package com.autism_rehablito_app.service;

import com.autism_rehablito_app.dto.ProgressRecordResponse;

import java.util.List;

public interface ProgressRecordService {

    List<ProgressRecordResponse> getMyChildProgress(
            Long userId,
            Long childId
    );
}

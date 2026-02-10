package com.trademind.user.service;

import com.trademind.user.dto.UserSummaryResponse;

public interface UserSummaryService {

    UserSummaryResponse getMySummary(Long userId);
}

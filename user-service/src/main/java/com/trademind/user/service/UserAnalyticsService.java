package com.trademind.user.service;

import com.trademind.user.dto.UserCountResponse;
import com.trademind.user.dto.UserGraphDto;
import com.trademind.user.dto.UserGrowthResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface UserAnalyticsService {

    UserCountResponse getUserCounts();

    UserGrowthResponse getUserGrowth(int days);

    UserCountResponse getUserCountsByRange(
            LocalDateTime start,
            LocalDateTime end);

    List<UserGraphDto> getUserGraph();
}
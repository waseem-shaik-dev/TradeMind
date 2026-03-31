package com.trademind.user.serviceImpl;

import com.trademind.user.dto.UserCountResponse;
import com.trademind.user.dto.UserGrowthResponse;
import com.trademind.user.repository.UserRepository;
import com.trademind.user.service.UserAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserAnalyticsServiceImpl implements UserAnalyticsService {

    private final UserRepository userRepository;

    @Override
    public UserCountResponse getUserCounts() {

        return userRepository.countUsersByRole();
    }

    @Override
    public UserGrowthResponse getUserGrowth(int days) {

        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        List<Object[]> results = userRepository.getUserGrowth(startDate);

        Map<String, Long> growthMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            growthMap.put(row[0].toString(), (Long) row[1]);
        }

        return UserGrowthResponse.builder()
                .dailyGrowth(growthMap)
                .build();
    }

    @Override
    public UserCountResponse getUserCountsByRange(
            LocalDateTime start,
            LocalDateTime end) {

        return userRepository.countUsersByRoleBetween(start, end);
    }
}
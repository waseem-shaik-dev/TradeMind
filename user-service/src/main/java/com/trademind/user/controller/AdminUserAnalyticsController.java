package com.trademind.user.controller;

import com.trademind.user.dto.UserCountResponse;
import com.trademind.user.dto.UserGrowthResponse;
import com.trademind.user.service.UserAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserAnalyticsController {

    private final UserAnalyticsService analyticsService;

    // ✅ REQUIRED BY ANALYTICS SERVICE
    @GetMapping("/count-by-role")
    public UserCountResponse getUserCounts() {
        return analyticsService.getUserCounts();
    }

    // 🔥 OPTIONAL (for charts)
    @GetMapping("/growth")
    public UserGrowthResponse getUserGrowth(
            @RequestParam(defaultValue = "30") int days) {

        return analyticsService.getUserGrowth(days);
    }

    @GetMapping("/count-by-role-range")
    public UserCountResponse getUserCountsByRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        return analyticsService.getUserCountsByRange(startDate, endDate);
    }
}
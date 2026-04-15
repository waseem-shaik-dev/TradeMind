package com.trademind.analytics.client.user;

import com.trademind.analytics.client.user.dto.UserCountResponse;
import com.trademind.analytics.client.user.dto.UserGraphDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8082",
        configuration = com.trademind.analytics.client.config.FeignConfig.class
)
public interface UserClient {

    @GetMapping("/api/admin/users/analytics/count-by-role")
    UserCountResponse getUserCounts();

    @GetMapping("/api/admin/users/analytics/count-by-role-range")
    UserCountResponse getUserCountsByRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    );

    @GetMapping("/api/admin/users/analytics/graph")
    List<UserGraphDto> getUserGraph();
}
package com.trademind.analytics.client.user;

import com.trademind.analytics.client.user.dto.UserCountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8082",
        configuration = com.trademind.analytics.client.config.FeignConfig.class
)
public interface UserClient {

    @GetMapping("/api/admin/users/count-by-role")
    UserCountResponse getUserCounts();
}
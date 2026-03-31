package com.trademind.analytics.client.audit;

import com.trademind.analytics.client.audit.dto.AuditLogResponseDto;
import com.trademind.analytics.dto.common.RecentActivityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "audit-service", url = "http://localhost:8089")
public interface AuditClient {

    @GetMapping("/api/audit-logs/recent")
    List<AuditLogResponseDto> getRecentActivities(@RequestParam int limit);
}
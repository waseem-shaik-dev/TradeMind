package com.trademind.audit.controller;

import com.trademind.audit.dto.AuditLogResponseDto;
import com.trademind.audit.service.AuditAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditAnalyticsController {

    private final AuditAnalyticsService service;

    @GetMapping("/recent")
    public List<AuditLogResponseDto> getRecent(
            @RequestParam(defaultValue = "10") int limit) {
        return service.getRecentActivities(limit);
    }

    @GetMapping("/recent/service")
    public List<AuditLogResponseDto> getByService(
            @RequestParam String serviceName,
            @RequestParam(defaultValue = "10") int limit) {

        return service.getRecentByService(serviceName, limit);
    }
}

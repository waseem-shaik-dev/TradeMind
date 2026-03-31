package com.trademind.audit.service;

import com.trademind.audit.dto.AuditLogResponseDto;

import java.util.List;

public interface AuditAnalyticsService {

    List<AuditLogResponseDto> getRecentActivities(int limit);

    List<AuditLogResponseDto> getRecentByService(String serviceName, int limit);
}
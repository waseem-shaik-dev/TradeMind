package com.trademind.audit.serviceImpl;


import com.trademind.audit.dto.AuditLogResponseDto;
import com.trademind.audit.repository.AuditLogRepository;
import com.trademind.audit.service.AuditAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditAnalyticsServiceImpl implements AuditAnalyticsService {

    private final AuditLogRepository repository;

    @Override
    public List<AuditLogResponseDto> getRecentActivities(int limit) {

        return repository.getRecentActivities(
                org.springframework.data.domain.PageRequest.of(0, limit)
        ).stream().map(this::map).toList();
    }

    @Override
    public List<AuditLogResponseDto> getRecentByService(String serviceName, int limit) {

        return repository.getRecentByService(serviceName,
                org.springframework.data.domain.PageRequest.of(0, limit)
        ).stream().map(this::map).toList();
    }

    private AuditLogResponseDto map(com.trademind.audit.entity.AuditLog a) {
        return AuditLogResponseDto.builder()
                .id(a.getId())
                .timestamp(a.getTimestamp())
                .serviceName(a.getServiceName())
                .action(a.getAction())
                .entityType(a.getEntityType())
                .entityId(a.getEntityId())
                .actor(a.getActor())
                .status(a.getStatus())
                .build();
    }
}

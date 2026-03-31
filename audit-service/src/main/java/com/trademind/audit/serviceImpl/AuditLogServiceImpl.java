package com.trademind.audit.serviceImpl;

import com.trademind.audit.dto.AuditLogResponseDto;
import com.trademind.audit.dto.AuditLogSearchRequestDto;
import com.trademind.audit.entity.AuditLog;
import com.trademind.audit.repository.AuditLogRepository;
import com.trademind.audit.service.AuditLogService;
import com.trademind.audit.specification.AuditLogSpecification;
import com.trademind.audit.util.PageableUtil;
import com.trademind.events.audit.AuditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    // =========================================================
    // 1. PROCESS EVENT (Kafka)
    // =========================================================
    @Override
    public void processAuditEvent(AuditEvent event) {

        if (event == null || event.eventId() == null) {
            log.warn("Invalid audit event received");
            return;
        }

        // ✅ Idempotency check (avoid duplicate logs)
        if (auditLogRepository.existsByEventId(event.eventId())) {
            log.warn("Duplicate audit event ignored: {}", event.eventId());
            return;
        }

        try {
            AuditLog auditLog = mapToEntity(event);

            auditLogRepository.save(auditLog);

            log.info("Audit log saved successfully: {}", event.eventId());

        } catch (Exception ex) {
            log.error("Error saving audit log: {}", event.eventId(), ex);
            throw ex; // Important for retry/DLQ
        }
    }

    // =========================================================
    // 2. SEARCH API
    // =========================================================
    @Override
    public Page<AuditLogResponseDto> search(AuditLogSearchRequestDto request) {

        Pageable pageable = PageableUtil.buildPageable(
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getSortDirection()
        );

        var spec = AuditLogSpecification.build(request);

        return auditLogRepository.findAll(spec, pageable)
                .map(this::mapToDto);
    }

    // =========================================================
    // 3. MAPPERS
    // =========================================================

    private AuditLog mapToEntity(AuditEvent event) {
        return AuditLog.builder()
                .eventId(event.eventId()) // 🔥 critical for idempotency
                .timestamp(event.timestamp())
                .serviceName(event.serviceName())
                .action(event.action())
                .entityType(event.entityType())
                .entityId(event.entityId())
                .actor(event.actor())
                .status(event.status())
                .beforeState(event.beforeState())
                .afterState(event.afterState())
                .metadata(event.metadata())
                .build();
    }

    private AuditLogResponseDto mapToDto(AuditLog entity) {
        return AuditLogResponseDto.builder()
                .id(entity.getId())
                .timestamp(entity.getTimestamp())
                .serviceName(entity.getServiceName())
                .action(entity.getAction())
                .entityType(entity.getEntityType())
                .entityId(entity.getEntityId())
                .actor(entity.getActor())
                .status(entity.getStatus())
                .beforeState(entity.getBeforeState())
                .afterState(entity.getAfterState())
                .metadata(entity.getMetadata())
                .build();
    }
}
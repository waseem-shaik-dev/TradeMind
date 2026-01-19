package com.trademind.audit.dto;

public record AuditEventDto(
        String serviceName,
        String action,
        String entityName,
        String entityId,
        Long performedBy,
        String role,
        String ipAddress,
        String metadata
) {}

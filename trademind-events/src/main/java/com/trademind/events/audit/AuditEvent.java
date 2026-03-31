package com.trademind.events.audit;

import com.trademind.events.audit.enums.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AuditEvent(

        UUID eventId,
        Instant timestamp,

        String serviceName,

        AuditAction action,
        EntityType entityType,
        String entityId,

        Map<String, Object> actor,

        AuditStatus status,

        Map<String, Object> beforeState,
        Map<String, Object> afterState,

        Map<String, Object> metadata
) {}
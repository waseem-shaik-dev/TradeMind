package com.trademind.analytics.client.audit.dto;

import com.trademind.events.audit.enums.AuditAction;
import com.trademind.events.audit.enums.AuditStatus;
import com.trademind.events.audit.enums.EntityType;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDto {

    private UUID id;
    private Instant timestamp;

    private String serviceName;

    private AuditAction action;
    private EntityType entityType;
    private String entityId;

    private Map<String, Object> actor;

    private AuditStatus status;

    private Map<String, Object> beforeState;
    private Map<String, Object> afterState;

    private Map<String, Object> metadata;
}
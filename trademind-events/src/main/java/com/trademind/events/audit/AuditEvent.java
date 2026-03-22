package com.trademind.events.audit;

import com.trademind.events.audit.enums.*;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {

    private UUID eventId;
    private Instant timestamp;

    private String serviceName;

    private AuditAction action;
    private EntityType entityType;
    private String entityId;

    private String userId;
    private String userRole;
    private String ipAddress;

    private AuditStatus status;

    private Map<String, Object> beforeState;
    private Map<String, Object> afterState;

    private Map<String, Object> metadata;
}
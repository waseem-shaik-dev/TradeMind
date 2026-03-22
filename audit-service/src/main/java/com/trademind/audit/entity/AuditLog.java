package com.trademind.audit.entity;


import com.trademind.events.audit.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_user_id", columnList = "userId"),
        @Index(name = "idx_audit_entity_id", columnList = "entityId"),
        @Index(name = "idx_audit_timestamp", columnList = "timestamp"),
        @Index(name = "idx_audit_action", columnList = "action")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityType entityType;

    @Column(nullable = false)
    private String entityId;

    private String userId;

    private String userRole;

    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    /**
     * JSONB fields for before & after states
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> beforeState;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> afterState;

    /**
     * Additional metadata (traceId, device, etc.)
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @PrePersist
    public void prePersist() {
        this.timestamp = Instant.now();
    }
}
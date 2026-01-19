package com.trademind.audit.kafka;

import com.trademind.audit.dto.AuditEventDto;
import com.trademind.audit.entity.AuditLog;
import com.trademind.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditEventConsumer {

    private final AuditLogRepository auditRepo;

    @KafkaListener(
            topics = "AUDIT_LOG_TOPIC",
            groupId = "audit-service"
    )
    public void consume(AuditEventDto event) {

        auditRepo.save(
                AuditLog.builder()
                        .serviceName(event.serviceName())
                        .action(event.action())
                        .entityName(event.entityName())
                        .entityId(event.entityId())
                        .performedBy(event.performedBy())
                        .role(event.role())
                        .ipAddress(event.ipAddress())
                        .metadata(event.metadata())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}

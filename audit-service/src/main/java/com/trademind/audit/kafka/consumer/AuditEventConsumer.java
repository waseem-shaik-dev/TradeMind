package com.trademind.audit.kafka.consumer;

import com.trademind.audit.service.AuditLogService;

import com.trademind.events.audit.AuditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventConsumer {

    private final AuditLogService auditLogService;

    @KafkaListener(
            topics = "${kafka.topics.audit-log}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(AuditEvent event) {

        try {
            log.info("Received audit event: {}", event.eventId());

            auditLogService.processAuditEvent(event);

        } catch (Exception ex) {
            log.error("Failed to process audit event: {}", event.eventId(), ex);

            // ❗ IMPORTANT:
            // Let exception propagate if using retry/DLQ
            throw ex;
        }
    }
}
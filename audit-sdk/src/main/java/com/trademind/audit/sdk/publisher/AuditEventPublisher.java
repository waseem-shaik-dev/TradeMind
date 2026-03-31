package com.trademind.audit.sdk.publisher;


import com.trademind.events.audit.AuditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventPublisher {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    private static final String TOPIC = "audit-log-topic";

    public void publish(AuditEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.eventId().toString(), event);
        } catch (Exception ex) {
          //  log.error("Failed to publish audit event", ex);
            // ❗ Do NOT break business flow
        }
    }
}
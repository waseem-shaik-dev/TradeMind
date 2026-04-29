package com.trademind.audit.sdk.aspect;

import com.trademind.audit.sdk.annotation.AuditLoggable;
import com.trademind.audit.sdk.context.AuditContextProvider;
import com.trademind.audit.sdk.publisher.AuditEventPublisher;
import com.trademind.audit.sdk.resolver.BeforeStateResolver;
import com.trademind.audit.sdk.util.SpelEvaluator;
import com.trademind.events.audit.AuditEvent;
import com.trademind.events.audit.enums.AuditStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditLoggingAspect {

    private final AuditEventPublisher publisher;
    private final AuditContextProvider contextProvider;
    private final BeforeStateResolver beforeStateResolver;

    @Around("@annotation(auditLoggable)")
    public Object logAudit(ProceedingJoinPoint joinPoint,
                           AuditLoggable auditLoggable) throws Throwable {

        Object result = null;
        AuditStatus status = AuditStatus.SUCCESS;

        try {
            result = joinPoint.proceed();
            return result;

        } catch (Exception ex) {
            status = AuditStatus.FAILURE;
            throw ex;

        } finally {
            try {
                AuditEvent event = buildEvent(joinPoint, auditLoggable, result, status);
                publisher.publish(event);
            } catch (Exception e) {
                log.error("Audit logging failed", e);
            }
        }
    }

    // =========================================================
    // BUILD EVENT
    // =========================================================
    private AuditEvent buildEvent(
            ProceedingJoinPoint joinPoint,
            AuditLoggable annotation,
            Object result,
            AuditStatus status
    ) {

        // 🔥 FIRST extract entityId
        String entityId = extractEntityId(joinPoint, annotation, result);

        // 🔥 THEN resolve before state
        Object beforeState = null;
        if (annotation.captureBefore()) {
            try {
                beforeState = beforeStateResolver.resolve(
                        entityId,
                        annotation.entityType().name()
                );
            } catch (Exception ex) {
                log.warn("Failed to fetch before state for entityId={}", entityId);
            }
        }

        Map<String, Object> actor = contextProvider.getActor(result);

        Object userId = actor.get("userId");

        if ((entityId == null || "UNKNOWN".equals(entityId)) && userId != null) {
            entityId = userId.toString();
        }


        return new AuditEvent(
                UUID.randomUUID(),
                Instant.now(),
                contextProvider.getServiceName(),
                annotation.action(),
                annotation.entityType(),
                entityId,
                actor,
                status,
                captureSafe(beforeState),
                captureAfterState(result, annotation),
                Map.of() // metadata
        );
    }

    // =========================================================
    // ENTITY ID EXTRACTION (SpEL)
    // =========================================================
    private String extractEntityId(
            ProceedingJoinPoint joinPoint,
            AuditLoggable annotation,
            Object result
    ) {
        try {
            Object value = SpelEvaluator.evaluate(
                    annotation.entityIdExpression(),
                    joinPoint,
                    result
            );

            return value != null ? value.toString() : "UNKNOWN";

        } catch (Exception ex) {
            log.warn("Failed to evaluate SpEL for entityId");
            return "UNKNOWN";
        }
    }

    // =========================================================
    // SAFE SERIALIZATION
    // =========================================================
    private Map<String, Object> captureSafe(Object obj) {
        if (obj == null) return null;

        try {
            return Map.of("data", obj.toString());
        } catch (Exception ex) {
            return Map.of("error", "serialization_failed");
        }
    }

    // =========================================================
    // AFTER STATE CAPTURE
    // =========================================================
    private Map<String, Object> captureAfterState(
            Object result,
            AuditLoggable annotation
    ) {
        if (!annotation.captureAfter() || result == null) {
            return null;
        }

        return captureSafe(result);
    }
}
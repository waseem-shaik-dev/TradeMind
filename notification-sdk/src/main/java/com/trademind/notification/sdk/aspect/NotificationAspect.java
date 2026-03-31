package com.trademind.notification.sdk.aspect;

import com.trademind.notification.sdk.annotation.Notify;
import com.trademind.notification.sdk.context.NotificationContextProvider;
import com.trademind.notification.sdk.publisher.NotificationEventPublisher;
import com.trademind.notification.sdk.resolver.NotificationSpelResolver;
import com.trademind.notification.sdk.util.MapUtil;
import com.trademind.notification.sdk.util.NotificationEventBuilder;
import com.trademind.events.notification.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {

    private final NotificationEventPublisher publisher;
    private final NotificationContextProvider contextProvider;

    @Around("@annotation(notify)")
    public Object handleNotification(
            ProceedingJoinPoint joinPoint,
            Notify notify
    ) throws Throwable {

        Object result = null;

        try {
            // 🔹 Execute business logic first
            result = joinPoint.proceed();
            return result;

        } catch (Exception ex) {
            // ❗ Notification should NOT block business flow
            throw ex;

        } finally {

            // 🔥 Notification logic must never break main flow
            try {

                log.info("🔥 Notification Aspect Triggered for {}", notify.type());

                if (!notify.enabled()) {
                    return result;
                }

                NotificationEvent event = buildEvent(joinPoint, notify, result);

                if (event != null) {
                    publisher.publish(event);
                }

            } catch (Exception e) {
                log.error("Notification aspect failed", e);
            }
        }
    }

    // =========================================================
    // BUILD EVENT
    // =========================================================
    private NotificationEvent buildEvent(
            ProceedingJoinPoint joinPoint,
            Notify notify,
            Object result
    ) {

        // 🔹 Extract recipient
        String recipient = resolveRecipient(joinPoint, notify, result);

        if (recipient == null || recipient.isBlank()) {
            log.warn("Skipping notification: recipient is null");
            return null;
        }

        // 🔹 Extract data
        Map<String, Object> data = resolveData(joinPoint, notify, result);

        // 🔹 Build event
        return NotificationEventBuilder.build(
                notify.type(),
                recipient,
                notify.template(),
                notify.subject(),
                data
        );
    }

    // =========================================================
    // RECIPIENT RESOLUTION
    // =========================================================
    private String resolveRecipient(
            ProceedingJoinPoint joinPoint,
            Notify notify,
            Object result
    ) {

        Object value = NotificationSpelResolver.resolve(
                notify.recipientExpression(),
                joinPoint,
                result
        );

        if (value != null) {
            return value.toString();
        }

        // 🔥 fallback to context
        return contextProvider.getCurrentUserEmail();
    }

    // =========================================================
    // DATA RESOLUTION
    // =========================================================
    private Map<String, Object> resolveData(
            ProceedingJoinPoint joinPoint,
            Notify notify,
            Object result
    ) {

        Object value = NotificationSpelResolver.resolve(
                notify.dataExpression(),
                joinPoint,
                result
        );

        return MapUtil.safeCast(value);
    }
}
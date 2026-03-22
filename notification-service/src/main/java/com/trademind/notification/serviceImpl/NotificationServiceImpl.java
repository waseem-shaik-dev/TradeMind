package com.trademind.notification.serviceImpl;

import com.trademind.events.notification.NotificationEvent;
import com.trademind.notification.entity.Notification;
import com.trademind.notification.enums.NotificationStatus;
import com.trademind.notification.repository.NotificationRepository;
import com.trademind.notification.service.NotificationService;
import com.trademind.notification.sender.EmailSender;
import com.trademind.notification.template.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailSender emailSender;
    private final TemplateService templateService;

    private static final int MAX_RETRY = 3;

    // =========================================================
    // PROCESS EVENT
    // =========================================================
    @Override
    public void processEvent(NotificationEvent event) {

        try {
            // 🔹 Build subject
            String subject = resolveSubject(event);

            // 🔹 Render template
            String body = templateService.render(
                    event.getType(),
                    event.getData()
            );

            // 🔹 Create entity
            Notification notification = Notification.builder()
                    .recipient(event.getRecipient())
                    .subject(subject)
                    .body(body)
                    .type(event.getType())
                    .status(NotificationStatus.PENDING)
                    .metadata(event.getMetadata())
                    .build();

            notificationRepository.save(notification);

            // 🔥 Send immediately
            sendNotification(notification);

        } catch (Exception ex) {
            log.error("Failed to process notification event", ex);
        }
    }

    // =========================================================
    // SEND EMAIL
    // =========================================================
    private void sendNotification(Notification notification) {

        try {
            emailSender.send(
                    notification.getRecipient(),
                    notification.getSubject(),
                    notification.getBody()
            );

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(Instant.now());

        } catch (Exception ex) {

            log.error("Email sending failed for {}", notification.getId(), ex);

            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(ex.getMessage());
            notification.setRetryCount(notification.getRetryCount() + 1);
        }

        notificationRepository.save(notification);
    }

    // =========================================================
    // RETRY FAILED NOTIFICATIONS
    // =========================================================
    @Override
    public void retryFailedNotifications() {

        List<Notification> failedNotifications =
                notificationRepository.findByStatusAndRetryCountLessThan(
                        NotificationStatus.FAILED,
                        MAX_RETRY
                );

        for (Notification notification : failedNotifications) {

            try {
                notification.setStatus(NotificationStatus.RETRYING);
                notificationRepository.save(notification);

                sendNotification(notification);

            } catch (Exception ex) {
                log.error("Retry failed for notification {}", notification.getId(), ex);
            }
        }
    }

    // =========================================================
    // SUBJECT RESOLUTION
    // =========================================================
    private String resolveSubject(NotificationEvent event) {

        if (event.getSubject() != null && !event.getSubject().isBlank()) {
            return event.getSubject();
        }

        // Default subject based on type
        return switch (event.getType()) {
            case USER_REGISTERED -> "Welcome to TradeMind 🎉";
            case USER_LOGIN -> "Login Alert";
            case ORDER_CREATED -> "Order Confirmation";
            case ORDER_CANCELLED -> "Order Cancelled";
            case PAYMENT_SUCCESS -> "Payment Successful";
            case PAYMENT_FAILED -> "Payment Failed";
            default -> "Notification from TradeMind";
        };
    }
}
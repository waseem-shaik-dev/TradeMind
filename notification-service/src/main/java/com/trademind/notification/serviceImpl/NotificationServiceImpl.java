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

        if(notificationRepository.existsByEventId(event.eventId())){
            log.warn("Duplicate notification event ignored: {}", event.eventId());
            return;
        }

        try {
            // 🔹 Build subject
            String subject = resolveSubject(event);

            // 🔹 Render template
            String body = templateService.render(
                    event.type(),
                    event.data()
            );

            // 🔹 Create entity
            Notification notification = Notification.builder()
                    .eventId(event.eventId())
                    .recipient(event.recipient())
                    .subject(subject)
                    .body(body)
                    .type(event.type())
                    .status(NotificationStatus.PENDING)
                    .metadata(event.metadata())
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

        if (event.subject() != null && !event.subject().isBlank()) {
            return event.subject();
        }

        // Default subject based on type
        return switch (event.type()) {
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
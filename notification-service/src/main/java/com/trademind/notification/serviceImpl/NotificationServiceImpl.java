package com.trademind.notification.serviceImpl;

import com.trademind.notification.entity.Notification;
import com.trademind.notification.entity.NotificationLog;
import com.trademind.notification.enums.NotificationStatus;
import com.trademind.notification.enums.NotificationType;
import com.trademind.notification.repository.NotificationLogRepository;
import com.trademind.notification.repository.NotificationRepository;
import com.trademind.notification.service.EmailService;
import com.trademind.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepo;
    private final NotificationLogRepository logRepo;
    private final EmailService emailService;

    @Override
    public void sendNotification(
            String email,
            NotificationType type,
            String message) {

        Notification notification = notificationRepo.save(
                Notification.builder()
                        .recipientEmail(email)
                        .type(type)
                        .message(message)
                        .status(NotificationStatus.SENT)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        try {
            emailService.sendEmail(
                    email,
                    "TradeMind Notification",
                    message
            );

            logRepo.save(new NotificationLog(
                    null,
                    notification.getId(),
                    LocalDateTime.now(),
                    "SUCCESS"
            ));

        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notificationRepo.save(notification);
        }
    }
}


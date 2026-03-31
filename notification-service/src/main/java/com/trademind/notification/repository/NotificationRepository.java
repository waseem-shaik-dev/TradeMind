package com.trademind.notification.repository;

import com.trademind.notification.entity.Notification;
import com.trademind.notification.enums.NotificationStatus;
import com.trademind.events.notification.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID>,
        JpaSpecificationExecutor<Notification> {

    // =========================================================
    // 🔁 RETRY SUPPORT
    // =========================================================

    /**
     * Fetch notifications that are pending or retrying
     */
    List<Notification> findByStatusIn(List<NotificationStatus> statuses);

    /**
     * Fetch failed notifications with retry limit
     */
    List<Notification> findByStatusAndRetryCountLessThan(
            NotificationStatus status,
            int retryCount
    );

    // =========================================================
    // 🔍 ADMIN SEARCH (Pagination)
    // =========================================================

    Page<Notification> findByRecipient(
            String recipient,
            Pageable pageable
    );

    Page<Notification> findByStatus(
            NotificationStatus status,
            Pageable pageable
    );

    Page<Notification> findByType(
            NotificationType type,
            Pageable pageable
    );

    Page<Notification> findByRecipientAndStatus(
            String recipient,
            NotificationStatus status,
            Pageable pageable
    );

    Page<Notification> findByTypeAndStatus(
            NotificationType type,
            NotificationStatus status,
            Pageable pageable
    );

    boolean existsByEventId(UUID eventId);
}
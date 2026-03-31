package com.trademind.notification.entity;

import com.trademind.events.notification.enums.NotificationType;
import com.trademind.notification.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notification_status", columnList = "status"),
        @Index(name = "idx_notification_recipient", columnList = "recipient"),
        @Index(name = "idx_notification_created_at", columnList = "createdAt")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private UUID eventId;

    // 🔹 Email recipient
    @Column(nullable = false)
    private String recipient;

    // 🔹 Subject
    @Column(nullable = false)
    private String subject;

    // 🔹 Email body (rendered template)
    @Column(columnDefinition = "TEXT")
    private String body;

    // 🔹 Notification type (ORDER, USER, etc.)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    // 🔹 Current status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    // 🔹 Retry count
    @Column(nullable = false)
    private int retryCount;

    // 🔹 Error message (if failed)
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    // 🔹 Flexible metadata (orderId, etc.)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    // 🔹 Audit fields
    private Instant createdAt;
    private Instant sentAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = NotificationStatus.PENDING;
        this.retryCount = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
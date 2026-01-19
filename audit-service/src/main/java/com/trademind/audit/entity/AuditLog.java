package com.trademind.audit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;

    private String action;       // CREATE_BILL, UPDATE_STOCK, LOGIN, etc.

    private String entityName;   // Bill, User, Inventory, Order

    private String entityId;     // ID of affected entity

    private Long performedBy;    // userId (nullable for system events)

    private String role;         // ADMIN / RETAILER / CUSTOMER

    private String ipAddress;

    private LocalDateTime timestamp;

    @Column(columnDefinition = "TEXT")
    private String metadata;     // JSON string (extra context)
}

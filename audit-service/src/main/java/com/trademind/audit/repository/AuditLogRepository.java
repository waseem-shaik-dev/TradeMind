package com.trademind.audit.repository;

import com.trademind.audit.entity.AuditLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends
        JpaRepository<AuditLog, UUID>,
        JpaSpecificationExecutor<AuditLog> {

    boolean existsByEventId(UUID eventId);

    // ================= RECENT ACTIVITIES =================
    @Query("""
        SELECT a FROM AuditLog a
        ORDER BY a.timestamp DESC
    """)
    List<AuditLog> getRecentActivities(Pageable pageable);


    // ================= FILTER BY SERVICE =================
    @Query("""
        SELECT a FROM AuditLog a
        WHERE a.serviceName = :serviceName
        ORDER BY a.timestamp DESC
    """)
    List<AuditLog> getRecentByService(@Param("serviceName") String serviceName, Pageable pageable);


    // ================= FILTER BY ENTITY =================
    @Query("""
        SELECT a FROM AuditLog a
        WHERE a.entityType = :entityType
        ORDER BY a.timestamp DESC
    """)
    List<AuditLog> getRecentByEntity(@Param("entityType") com.trademind.events.audit.enums.EntityType entityType, Pageable pageable);
}
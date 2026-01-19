package com.trademind.audit.controller;

import com.trademind.audit.entity.AuditLog;
import com.trademind.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService service;

    @GetMapping
    public List<AuditLog> getAll() {
        return service.getAll();
    }

    @GetMapping("/user/{userId}")
    public List<AuditLog> byUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }

    @GetMapping("/service/{name}")
    public List<AuditLog> byService(@PathVariable String name) {
        return service.getByService(name);
    }

    @GetMapping("/entity")
    public List<AuditLog> byEntity(
            @RequestParam String entity,
            @RequestParam String entityId) {
        return service.getByEntity(entity, entityId);
    }
}


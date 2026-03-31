package com.trademind.audit.sdk.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuditContextProvider {

    // 🔥 Inject service name dynamically
    @Value("${spring.application.name:unknown-service}")
    private String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public Map<String, Object> getActor() {

        Map<String, Object> ctx = AuditContext.get();

        if (ctx == null) {
            return Map.of(
                    "userId", "SYSTEM",
                    "role", "SYSTEM",
                    "ip", "UNKNOWN",
                    "requestId", "N/A"
            );
        }

        Map<String, Object> actor = new HashMap<>();

        actor.put("userId", ctx.getOrDefault("userId", "SYSTEM"));
        actor.put("role", ctx.getOrDefault("role", "SYSTEM"));
        actor.put("ip", ctx.getOrDefault("ip", "UNKNOWN"));
        actor.put("requestId", ctx.getOrDefault("requestId", "N/A"));

        return actor;
    }
}
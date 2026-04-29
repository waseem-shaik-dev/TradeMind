package com.trademind.audit.sdk.context;

import com.trademind.audit.sdk.contract.AuditActorAware;
import com.trademind.audit.sdk.dto.AuthResponse;
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

    public Map<String, Object> getActor(Object result) {

        Map<String, Object> ctx = AuditContext.get();

        Map<String, Object> actor = new HashMap<>();

        if (ctx != null && ctx.get("userId") != null) {
            actor.put("userId", ctx.get("userId"));
            actor.put("role", ctx.get("role"));
        } else if (result instanceof  AuditActorAware actorAware) {
            // 🔥 LOGIN FALLBACK
            actor.put("userId", actorAware.getAuditUserId());
            actor.put("role", actorAware.getAuditUserRole());
        } else {
            actor.put("userId", "UNKNOWN");
            actor.put("role", "UNKNOWN");
        }

        actor.put("ip", ctx != null ? ctx.get("ip") : "UNKNOWN");
        actor.put("requestId", ctx != null ? ctx.get("requestId") : "N/A");

        return actor;
    }
}
package com.trademind.audit.sdk.context;

import org.springframework.stereotype.Component;

@Component
public class AuditContextProvider {

    public String getCurrentUserId() {
        // integrate with your security context
        return "mock-user";
    }

    public String getUserRole() {
        return "ADMIN";
    }

    public String getIpAddress() {
        return "127.0.0.1";
    }

    public String getServiceName() {
        return "order-service"; // can inject via config
    }
}
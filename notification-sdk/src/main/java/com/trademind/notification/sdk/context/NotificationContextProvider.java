package com.trademind.notification.sdk.context;

import org.springframework.stereotype.Component;

@Component
public class NotificationContextProvider {

    /**
     * 🔹 Get current user email (optional fallback)
     */
    public String getCurrentUserEmail() {
        // integrate with security context later
        return "default@trademind.com";
    }

    /**
     * 🔹 Service name (useful for debugging)
     */
    public String getServiceName() {
        return "unknown-service"; // override via config later
    }

    /**
     * 🔹 Additional metadata (optional)
     */
    public String getRequestId() {
        return "N/A"; // can integrate with tracing later
    }
}
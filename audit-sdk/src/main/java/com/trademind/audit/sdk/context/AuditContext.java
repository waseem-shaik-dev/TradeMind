package com.trademind.audit.sdk.context;

import java.util.Map;

public class AuditContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    public static void set(Map<String, Object> data) {
        CONTEXT.set(data);
    }

    public static Map<String, Object> get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
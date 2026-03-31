package com.trademind.audit.sdk.context;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class AuditContextFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        try {
            Map<String, Object> context = new HashMap<>();

            // 🔥 Extract from headers
            context.put("userId", req.getHeader("X-User-Id"));
            context.put("role", req.getHeader("X-User-Role"));
            context.put("requestId",
                    req.getHeader("X-Request-Id") != null
                            ? req.getHeader("X-Request-Id")
                            : UUID.randomUUID().toString()
            );

            context.put("ip", request.getRemoteAddr());

            AuditContext.set(context);

            chain.doFilter(request, response);

        } finally {
            AuditContext.clear(); // 🔥 MUST
        }
    }
}
package com.trademind.gateway.filter;

import com.trademind.gateway.security.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        String path = exchange.getRequest().getURI().getPath();
        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        boolean isAuthApi = path.startsWith("/api/auth");

        // 🔓 Public auth endpoints WITHOUT token
        if (isAuthApi && (authHeader == null || !authHeader.startsWith("Bearer "))) {
            return chain.filter(exchange);
        }

        // 🔐 Token is required OR present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = jwtService.validateAndExtractClaims(token);

            exchange = exchange.mutate()
                    .request(
                            exchange.getRequest().mutate()
                                    .header("X-USER-ID",
                                            String.valueOf(
                                                    claims.get("userId", Long.class)))
                                    .header("X-USER-ROLE",
                                            claims.get("role", String.class))
                                    .build()
                    )
                    .build();

        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

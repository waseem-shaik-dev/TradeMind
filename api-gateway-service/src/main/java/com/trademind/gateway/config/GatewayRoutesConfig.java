package com.trademind.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("http://localhost:8081"))

                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("http://localhost:8082"))

                .route("user-admin-service", r -> r
                        .path("/api/admin/users/**")
                        .uri("http://localhost:8082"))

                .route("product-service", r -> r
                        .path("/api/products/**")
                        .uri("http://localhost:8083"))

                .route("inventory-service", r -> r
                        .path("/api/inventories/**")
                        .uri("http://localhost:8084"))

                .route("billing-service", r -> r
                        .path("/api/billing/**")
                        .uri("http://localhost:8085"))


                .route("order-service", r -> r
                        .path("/api/orders/**")
                        .uri("http://localhost:8087"))

                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri("http://localhost:8088"))

                .route("audit-service", r -> r
                        .path("/api/audit-logs/**")
                        .uri("http://localhost:8089"))

                .route("analytics-service", r -> r
                        .path("/api/analytics/**")
                        .uri("http://localhost:8090"))

                .route("cart-service",r->r
                        .path("/api/cart/**")
                        .uri("http://localhost:8091"))

                .route("checkout-service",r->r
                        .path("/api/checkout/**")
                        .uri("http://localhost:8093"))

                .route("payment-service",r->r
                        .path("/api/payments/**")
                        .uri("http://localhost:8094"))


                .build();
    }
}

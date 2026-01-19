package com.trademind.order.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Component
public class ProductClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://PRODUCT-SERVICE")
            .build();

    public BigDecimal getPrice(Long productId) {
        return webClient.get()
                .uri("/api/products/{id}/price", productId)
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .block();
    }
}

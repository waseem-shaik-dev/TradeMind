package com.trademind.cart.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(
        name = "catalogue-client",
        url = "http://localhost:8092"
)
public interface CatalogueClient {

    @GetMapping("/api/catalogue/internal/product/{productId}")
    CatalogueProductDto getProduct(@PathVariable Long productId);

    @PostMapping("/api/catalogue/internal/products")
    List<CatalogueProductDto> getProducts(
            @RequestBody List<Long> productIds
    );

    @Data
    class CatalogueProductDto {
        private Long productId;
        private String name;
        private String sku;
        private BigDecimal price;
        private List<String> images;
        private Long sourceId;
        private String sourceType;
    }
}

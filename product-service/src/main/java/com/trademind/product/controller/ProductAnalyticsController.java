package com.trademind.product.controller;

import com.trademind.product.dto.*;
import com.trademind.product.service.ProductAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductAnalyticsController {

    private final ProductAnalyticsService service;

    @GetMapping("/count")
    public long getTotalProducts() {
        return service.getTotalProducts();
    }

    @GetMapping("/stats")
    public ProductCountDto getStats() {
        return service.getProductStats();
    }

    @GetMapping("/categories/distribution")
    public List<CategoryCountDto> getCategoryDistribution() {
        return service.getCategoryDistribution();
    }
}
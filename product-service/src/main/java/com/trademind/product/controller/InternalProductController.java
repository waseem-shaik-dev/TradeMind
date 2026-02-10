package com.trademind.product.controller;

import com.trademind.product.dto.ProductDetailResponse;
import com.trademind.product.dto.internal.CatalogueProductForCartResponse;
import com.trademind.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
public class InternalProductController {

    private final ProductService productService;

    // INTERNAL – Batch fetch for cart & order
    @PostMapping("/batch")
    public List<CatalogueProductForCartResponse> getProductsForCart(
            @RequestBody List<Long> productIds) {

        return productService.getProductsForCart(productIds);
    }
}

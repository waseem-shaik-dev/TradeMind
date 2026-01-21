package com.trademind.product.controller;

import com.trademind.product.dto.*;
import com.trademind.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    /* ---------------- CREATE PRODUCT ---------------- */

    @PostMapping
    public ProductDetailResponse createProduct(
            @RequestBody ProductCreateRequest request) {
        return productService.createProduct(request);
    }

    /* ---------------- UPDATE PRODUCT ---------------- */

    @PutMapping("/{productId}")
    public ProductDetailResponse updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest request) {
        return productService.updateProduct(productId, request);
    }

    /* ---------------- DEACTIVATE PRODUCT ---------------- */

    @DeleteMapping("/{productId}")
    public void deactivateProduct(@PathVariable Long productId) {
        productService.deactivateProduct(productId);
    }

    /* ---------------- SET PRODUCT PRICE ---------------- */

    @PostMapping("/{productId}/price")
    public ProductPriceResponse setPrice(
            @PathVariable Long productId,
            @RequestParam BigDecimal price,
            @RequestParam LocalDateTime effectiveFrom) {

        return productService.setProductPrice(productId, price, effectiveFrom);
    }
}

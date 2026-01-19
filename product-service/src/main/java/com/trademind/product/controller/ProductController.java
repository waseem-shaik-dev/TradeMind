package com.trademind.product.controller;

import com.trademind.product.entity.Product;
import com.trademind.product.entity.ProductPriceHistory;
import com.trademind.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping
    public List<Product> getActive() {
        return productService.getActiveProducts();
    }

    @PostMapping("/{id}/price")
    public ProductPriceHistory setPrice(
            @PathVariable Long id,
            @RequestParam BigDecimal price,
            @RequestParam LocalDateTime effectiveFrom) {

        return productService.setProductPrice(id, price, effectiveFrom);
    }

    @GetMapping("/{id}/price")
    public BigDecimal getPrice(@PathVariable Long id) {
        return productService.getCurrentPrice(id);
    }
}


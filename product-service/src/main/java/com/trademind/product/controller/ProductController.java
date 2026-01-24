package com.trademind.product.controller;

import com.trademind.product.dto.*;
import com.trademind.product.enums.OwnerType;
import com.trademind.product.service.ProductQueryService;
import com.trademind.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductService productService;

    // For catalogue summary
    @GetMapping("/catalogue/summary")
    public List<ProductSummaryResponse> getCatalogueProductSummaries(
            @RequestParam OwnerType ownerType) {
        return productService.getProductSummariesByOwnerType(ownerType);
    }

    // For catalogue product detail
    @GetMapping("/catalogue/{productId}")
    public ProductDetailResponse getCatalogueProductDetail(
            @PathVariable Long productId) {
        return productService.getProductDetailById(productId);
    }


    @GetMapping("/owner/{ownerId}/summary")
    public List<ProductSummaryResponse> getProductSummariesByOwner(
            @PathVariable Long ownerId,
            @RequestParam OwnerType ownerType) {

        return productService.getProductSummariesByOwner(ownerId, ownerType);
    }

    // 🔹 DETAIL VIEW (Management / Admin)
    @GetMapping("/owner/{ownerId}/details")
    public List<ProductDetailResponse> getProductDetailsByOwner(
            @PathVariable Long ownerId,
            @RequestParam OwnerType ownerType) {

        return productService.getProductDetailsByOwner(ownerId, ownerType);
    }

    /* ---------------- PRODUCT DETAILS ---------------- */

    @GetMapping("/{productId}")
    public ProductDetailResponse getProduct(
            @PathVariable Long productId) {
        return productQueryService.getProductById(productId);
    }

    /* ---------------- SEARCH & FILTER ---------------- */

    @GetMapping
    public Page<ProductSummaryResponse> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Boolean active,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        ProductSearchRequest request =
                new ProductSearchRequest(
                        keyword,
                        categoryId,
                        brandId,
                        null,
                        null,
                        active
                );

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productQueryService.searchProducts(request, pageable);
    }
}

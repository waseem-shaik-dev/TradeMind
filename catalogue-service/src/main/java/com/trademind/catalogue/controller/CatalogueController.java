package com.trademind.catalogue.controller;

import com.trademind.catalogue.dto.CatalogueProductDetailResponse;
import com.trademind.catalogue.dto.CatalogueProductForCartResponse;
import com.trademind.catalogue.dto.CatalogueProductSummaryResponse;
import com.trademind.catalogue.dto.SellerCatalogueProductResponse;
import com.trademind.catalogue.service.CatalogueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
@RequiredArgsConstructor
public class CatalogueController {

    private final CatalogueService catalogueService;

    // Retailers browsing merchant products
    @GetMapping("/merchant")
    public List<CatalogueProductSummaryResponse> browseMerchantProducts() {
        return catalogueService.browseMerchantProducts();
    }

    // Customers browsing retailer products
    @GetMapping("/retailer")
    public List<CatalogueProductSummaryResponse> browseRetailerProducts() {
        return catalogueService.browseRetailerProducts();
    }

    // Product click
    @GetMapping("/product/{productId}")
    public CatalogueProductDetailResponse getProductDetail(
            @PathVariable Long productId) {
        return catalogueService.getCatalogueProductDetail(productId);
    }

    // INTERNAL – Used by Cart Service
    @GetMapping("/internal/product/{productId}")
    public CatalogueProductForCartResponse getProductForCart(
            @PathVariable Long productId) {
        return catalogueService.getProductForCart(productId);
    }

    // INTERNAL – Batch product fetch for cart
    @PostMapping("/internal/products")
    public List<CatalogueProductForCartResponse> getProductsForCart(
            @RequestBody List<Long> productIds) {

        return catalogueService.getProductsForCart(productIds);
    }


    @GetMapping("/seller/products")
    public List<SellerCatalogueProductResponse> browseProductsForSeller(
            @RequestHeader("x-user-Id") Long sourceId){

        return catalogueService.browseProductsForSeller(sourceId);
    }


    @GetMapping("/seller/{sellerId}/products")
    public List<CatalogueProductSummaryResponse> browseSellerProducts(
            @PathVariable Long sellerId) {

        return catalogueService.browseProductsForSellerCatalogue(sellerId);
    }


    @GetMapping("/seller/{sellerId}/product/{productId}")
    public CatalogueProductDetailResponse getSellerProductDetail(
            @PathVariable Long sellerId,
            @PathVariable Long productId) {

        return catalogueService.getSellerProductDetail(sellerId, productId);
    }

}

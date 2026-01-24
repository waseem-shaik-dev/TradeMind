package com.trademind.catalogue.controller;

import com.trademind.catalogue.dto.CatalogueProductDetailResponse;
import com.trademind.catalogue.dto.CatalogueProductSummaryResponse;
import com.trademind.catalogue.service.CatalogueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

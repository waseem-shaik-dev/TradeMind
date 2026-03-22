package com.trademind.catalogue.service;

import com.trademind.catalogue.dto.CatalogueProductDetailResponse;
import com.trademind.catalogue.dto.CatalogueProductForCartResponse;
import com.trademind.catalogue.dto.CatalogueProductSummaryResponse;
import com.trademind.catalogue.dto.SellerCatalogueProductResponse;

import java.util.List;

public interface CatalogueService {

    List<CatalogueProductSummaryResponse> browseMerchantProducts();

    List<CatalogueProductSummaryResponse> browseRetailerProducts();

    CatalogueProductDetailResponse getCatalogueProductDetail(Long productId);

    CatalogueProductForCartResponse getProductForCart(Long productId);

    List<CatalogueProductForCartResponse> getProductsForCart(
            List<Long> productIds
    );

    List<SellerCatalogueProductResponse> browseProductsForSeller(Long sourceId);

    List<CatalogueProductSummaryResponse> browseProductsForSellerCatalogue(
            Long sellerId
    );

    CatalogueProductDetailResponse getSellerProductDetail(
            Long sellerId,
            Long productId
    );
}

package com.trademind.catalogue.service;

import com.trademind.catalogue.dto.CatalogueProductDetailResponse;
import com.trademind.catalogue.dto.CatalogueProductSummaryResponse;

import java.util.List;

public interface CatalogueService {

    List<CatalogueProductSummaryResponse> browseMerchantProducts();

    List<CatalogueProductSummaryResponse> browseRetailerProducts();

    CatalogueProductDetailResponse getCatalogueProductDetail(Long productId);
}

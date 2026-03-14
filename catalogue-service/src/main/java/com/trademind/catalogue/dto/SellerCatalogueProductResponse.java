package com.trademind.catalogue.dto;


import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
public record SellerCatalogueProductResponse(

        Long productId,
        String name,
        String sku,
        String description,

        BigDecimal currentPrice,

        String primaryImageUrl,

        Map<String,String> attributes,

        boolean sellerHasInventory,

        Long inventoryId,

        Integer availableQuantity,

        BigDecimal sellerPrice

) {}
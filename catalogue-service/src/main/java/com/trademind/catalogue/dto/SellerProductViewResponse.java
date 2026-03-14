package com.trademind.catalogue.dto;

import java.math.BigDecimal;
import java.util.Map;

public record SellerProductViewResponse(

        Long id,
        String name,
        String sku,
        String description,
        BigDecimal currentPrice,
        String primaryImageUrl,
        Map<String,String> attributes

) {}
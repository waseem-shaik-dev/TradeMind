package com.trademind.product.service;

import com.trademind.product.dto.*;

import java.util.List;

public interface ProductAnalyticsService {

    ProductCountDto getProductStats();

    long getTotalProducts();

    List<CategoryCountDto> getCategoryDistribution();
}
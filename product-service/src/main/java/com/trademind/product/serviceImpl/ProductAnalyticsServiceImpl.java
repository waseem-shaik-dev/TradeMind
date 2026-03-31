package com.trademind.product.service.impl;

import com.trademind.product.dto.*;
import com.trademind.product.repository.ProductRepository;
import com.trademind.product.service.ProductAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAnalyticsServiceImpl implements ProductAnalyticsService {

    private final ProductRepository repository;

    @Override
    public ProductCountDto getProductStats() {
        return repository.getProductStats();
    }

    @Override
    public long getTotalProducts() {
        return repository.getTotalProducts();
    }

    @Override
    public List<CategoryCountDto> getCategoryDistribution() {
        return repository.getCategoryDistribution();
    }
}
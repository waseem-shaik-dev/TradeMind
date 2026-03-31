package com.trademind.inventory.serviceImpl;

import com.trademind.inventory.dto.*;
import com.trademind.inventory.enums.OwnerType;
import com.trademind.inventory.repository.InventoryRepository;
import com.trademind.inventory.service.InventoryAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryAnalyticsServiceImpl implements InventoryAnalyticsService {

    private final InventoryRepository repository;

    @Override
    public List<LowStockResponse> getLowStockProducts(Long sellerId) {
        return repository.getLowStockProducts(sellerId);
    }

    @Override
    public int getLowStockCount(Long sellerId) {
        return repository.getLowStockCount(sellerId);
    }

    @Override
    public int getOutOfStockCount(Long sellerId) {
        return repository.getOutOfStockCount(sellerId);
    }

    @Override
    public InventorySummaryDto getSummary(Long sellerId) {
        return repository.getInventorySummary(sellerId);
    }

    @Override
    public long getProductCount(Long sellerId, OwnerType sourceRole) {
        return repository.countProductsBySource(sellerId, sourceRole);
    }
}
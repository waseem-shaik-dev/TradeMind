package com.trademind.inventory.service;

import com.trademind.inventory.dto.*;
import com.trademind.inventory.enums.OwnerType;

import java.util.List;

public interface InventoryAnalyticsService {

    List<LowStockResponse> getLowStockProducts(Long sellerId);

    int getLowStockCount(Long sellerId);

    int getOutOfStockCount(Long sellerId);

    InventorySummaryDto getSummary(Long sellerId);

    long getProductCount(Long sellerId, OwnerType sourceRole);
}
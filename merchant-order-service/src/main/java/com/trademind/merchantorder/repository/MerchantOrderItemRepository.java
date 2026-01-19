package com.trademind.merchantorder.repository;

import com.trademind.merchantorder.entity.MerchantOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MerchantOrderItemRepository
        extends JpaRepository<MerchantOrderItem, Long> {
    List<MerchantOrderItem> findByOrderId(Long orderId);
}


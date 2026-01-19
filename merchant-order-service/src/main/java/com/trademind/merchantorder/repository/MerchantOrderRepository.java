package com.trademind.merchantorder.repository;

import com.trademind.merchantorder.entity.MerchantOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MerchantOrderRepository
        extends JpaRepository<MerchantOrder, Long> {
    List<MerchantOrder> findByRetailerId(Long retailerId);
    List<MerchantOrder> findByMerchantId(Long merchantId);
}

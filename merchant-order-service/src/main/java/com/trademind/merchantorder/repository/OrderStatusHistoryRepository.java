package com.trademind.merchantorder.repository;

import com.trademind.merchantorder.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusHistoryRepository
        extends JpaRepository<OrderStatusHistory, Long> {}

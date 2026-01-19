package com.trademind.order.repository;

import com.trademind.order.entity.OrderTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTrackingRepository
        extends JpaRepository<OrderTracking, Long> {}

package com.trademind.order.repository;

import com.trademind.order.entity.OrderShipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderShipmentRepository
        extends JpaRepository<OrderShipment, Long> {}

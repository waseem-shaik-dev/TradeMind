package com.trademind.order.repository;

import com.trademind.order.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentRepository
        extends JpaRepository<OrderPayment, Long> {}

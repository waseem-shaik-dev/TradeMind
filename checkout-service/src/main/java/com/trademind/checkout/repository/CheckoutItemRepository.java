package com.trademind.checkout.repository;

import com.trademind.checkout.entity.CheckoutItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutItemRepository extends JpaRepository<CheckoutItem, Long> {

    List<CheckoutItem> findByCheckoutSessionId(Long checkoutSessionId);
}

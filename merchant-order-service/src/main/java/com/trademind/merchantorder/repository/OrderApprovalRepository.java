package com.trademind.merchantorder.repository;

import com.trademind.merchantorder.entity.OrderApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderApprovalRepository
        extends JpaRepository<OrderApproval, Long> {}

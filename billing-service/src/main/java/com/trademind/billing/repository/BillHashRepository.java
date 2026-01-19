package com.trademind.billing.repository;

import com.trademind.billing.entity.BillHash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillHashRepository extends JpaRepository<BillHash, Long> {
    Optional<BillHash> findByBillId(Long billId);
}
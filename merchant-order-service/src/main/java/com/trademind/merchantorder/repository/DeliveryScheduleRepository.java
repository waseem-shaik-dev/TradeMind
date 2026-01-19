package com.trademind.merchantorder.repository;

import com.trademind.merchantorder.entity.DeliverySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryScheduleRepository
        extends JpaRepository<DeliverySchedule, Long> {}

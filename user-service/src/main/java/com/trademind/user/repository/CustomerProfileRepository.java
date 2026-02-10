package com.trademind.user.repository;

import com.trademind.user.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    Optional<CustomerProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);

    // Loyalty / marketing
    List<CustomerProfile> findByLoyaltyPointsGreaterThan(Integer points);
}

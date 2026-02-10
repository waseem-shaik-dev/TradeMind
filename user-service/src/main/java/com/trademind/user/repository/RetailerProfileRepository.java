package com.trademind.user.repository;

import com.trademind.user.entity.RetailerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RetailerProfileRepository extends JpaRepository<RetailerProfile, Long> {

    Optional<RetailerProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);

    // Verification
    List<RetailerProfile> findByVerified(Boolean verified);

    Optional<RetailerProfile> findByGstNumber(String gstNumber);
}

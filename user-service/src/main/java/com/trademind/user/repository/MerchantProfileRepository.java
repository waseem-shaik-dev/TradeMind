package com.trademind.user.repository;

import com.trademind.user.entity.MerchantProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MerchantProfileRepository extends JpaRepository<MerchantProfile, Long> {

    Optional<MerchantProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);

    // Verification & onboarding
    List<MerchantProfile> findByVerified(Boolean verified);

    Optional<MerchantProfile> findByGstNumber(String gstNumber);
    Optional<MerchantProfile> findByLicenseNumber(String licenseNumber);
}

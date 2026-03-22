package com.trademind.user.repository;

import com.trademind.user.entity.MerchantProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MerchantProfileRepository extends JpaRepository<MerchantProfile, Long> {

    Optional<MerchantProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);


    @EntityGraph(attributePaths = {"storeAddress", "user"})
    Page<MerchantProfile> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"storeAddress", "user"})
    Page<MerchantProfile> findByBusinessNameContainingIgnoreCase(String keyword, Pageable pageable);


}

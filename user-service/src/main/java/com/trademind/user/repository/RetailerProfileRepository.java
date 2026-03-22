package com.trademind.user.repository;

import com.trademind.user.entity.RetailerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RetailerProfileRepository extends JpaRepository<RetailerProfile, Long> {

    Optional<RetailerProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);


    @EntityGraph(attributePaths = {"storeAddress", "user"})
    Page<RetailerProfile> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"storeAddress", "user"})
    Page<RetailerProfile> findByShopNameContainingIgnoreCase(String keyword, Pageable pageable);


}

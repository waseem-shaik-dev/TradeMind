package com.trademind.product.repository;

import com.trademind.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductId(Long productId);
    long countByProductId(Long productId);
    void deleteByPublicId(String publicId);
    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(Long productId);
    Optional<ProductImage> findFirstByProductIdOrderByDisplayOrderAsc(Long productId);
    @Modifying
    @Query("update ProductImage i set i.primaryImage = false where i.product.id = :productId")
    void clearPrimaryImages(@Param("productId") Long productId);

}

package com.trademind.product.repository;

import com.trademind.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductId(Long productId);

    void deleteByPublicId(String publicId);
    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(Long productId);

}

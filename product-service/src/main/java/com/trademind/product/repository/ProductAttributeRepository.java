package com.trademind.product.repository;

import com.trademind.product.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductAttributeRepository
        extends JpaRepository<ProductAttribute, Long> {

    List<ProductAttribute> findByProductId(Long productId);
}

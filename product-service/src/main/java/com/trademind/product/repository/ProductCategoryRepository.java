package com.trademind.product.repository;

import com.trademind.product.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository
        extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findByParentCategoryId(Long parentCategoryId);

    List<ProductCategory> findByParentCategoryIdIsNull();

    boolean existsByNameIgnoreCaseAndParentCategoryId(
            String name,
            Long parentCategoryId
    );
}
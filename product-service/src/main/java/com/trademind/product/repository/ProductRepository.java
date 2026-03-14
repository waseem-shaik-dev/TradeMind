package com.trademind.product.repository;

import com.trademind.product.dto.SellerProductProjection;
import com.trademind.product.entity.Product;
import com.trademind.product.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByIdIn(List<Long> ids);

    @Query("""
            SELECT new com.trademind.product.dto.projection.SellerProductProjection(
                p.id,
                p.name,
                p.sku,
                p.description
            )
            FROM Product p
            """)
    List<SellerProductProjection> findSellerProductProjection();


}

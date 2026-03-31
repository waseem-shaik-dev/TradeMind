package com.trademind.product.repository;

import com.trademind.product.dto.CategoryCountDto;
import com.trademind.product.dto.ProductCountDto;
import com.trademind.product.dto.SellerProductProjection;
import com.trademind.product.entity.Product;
import com.trademind.product.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByIdIn(List<Long> ids);

    @Query("""
            SELECT new com.trademind.product.dto.SellerProductProjection(
                p.id,
                p.name,
                p.sku,
                p.description
            )
            FROM Product p
            """)
    List<SellerProductProjection> findSellerProductProjection();

    // ================= GLOBAL COUNT =================
    @Query("""
        SELECT new com.trademind.product.dto.ProductCountDto(
            COUNT(p),
            COALESCE(SUM(CASE WHEN p.active = true THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN p.active = false THEN 1 ELSE 0 END), 0)
        )
        FROM Product p
    """)
    ProductCountDto getProductStats();


    // ================= CATEGORY DISTRIBUTION =================
    @Query("""
        SELECT new com.trademind.product.dto.CategoryCountDto(
            p.categoryId,
            COUNT(p)
        )
        FROM Product p
        GROUP BY p.categoryId
    """)
    List<CategoryCountDto> getCategoryDistribution();


    // ================= SIMPLE COUNTS =================
    @Query("SELECT COUNT(p) FROM Product p")
    long getTotalProducts();


    // OPTIONAL (if you later map ownership)
    @Query("""
        SELECT COUNT(p)
        FROM Product p
        WHERE p.brandId = :brandId
    """)
    long getProductsByBrand(@Param("brandId") Long brandId);

}

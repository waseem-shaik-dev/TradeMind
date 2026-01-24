package com.trademind.product.repository;

import com.trademind.product.entity.Product;
import com.trademind.product.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);
    List<Product> findByOwnerIdAndOwnerType(Long ownerId, OwnerType ownerType);
    boolean existsBySku(String sku);
    List<Product> findByOwnerType(OwnerType ownerType);
}

package com.trademind.inventory.repository;

import com.trademind.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByOwnerId(Long ownerId);
}

package com.trademind.inventory.repository;

import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByOwnerIdAndOwnerType(Long ownerId, OwnerType ownerType);
    List<Inventory> findByOwnerType(OwnerType ownerType);
}

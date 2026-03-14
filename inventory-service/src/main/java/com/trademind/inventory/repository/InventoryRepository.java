package com.trademind.inventory.repository;

import com.trademind.inventory.entity.Inventory;
import com.trademind.inventory.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<List<Inventory>> findByOwnerIdAndOwnerType(Long ownerId, OwnerType ownerType);
    List<Inventory> findByOwnerType(OwnerType ownerType);
}

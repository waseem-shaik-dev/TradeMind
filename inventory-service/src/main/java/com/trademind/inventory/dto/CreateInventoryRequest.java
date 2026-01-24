package com.trademind.inventory.dto;

import com.trademind.inventory.enums.OwnerType;

public record CreateInventoryRequest(
        Long ownerId,
        OwnerType ownerType,
        String location,
        String primaryImageUrl
) {}

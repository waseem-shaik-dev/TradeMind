package com.trademind.events.common;

public record SellerSnapshotDto(

        Long sourceId,
        String sourceType,
        String name,
        String email,
        String shopImageUrl,

        StoreAddressSnapshotDto storeAddress

) {}
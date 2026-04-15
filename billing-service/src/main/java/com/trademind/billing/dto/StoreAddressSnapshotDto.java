package com.trademind.billing.dto;

public record StoreAddressSnapshotDto(

        String line1,
        String line2,
        String city,
        String state,
        String pincode,
        String country

) {}
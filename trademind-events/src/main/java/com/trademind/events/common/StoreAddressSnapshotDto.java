package com.trademind.events.common;

public record StoreAddressSnapshotDto(

        String line1,
        String line2,
        String city,
        String state,
        String pincode,
        String country,
        Double latitude,
        Double longitude,
        String mapUrl

) {}
package com.trademind.user.dto;

public record RetailerCardDto(

        Long retailerId,

        String shopName,

        String shopEmail,

        String city,

        String state,

        String shopImageUrl

) {}
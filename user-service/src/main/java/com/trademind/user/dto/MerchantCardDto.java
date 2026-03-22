package com.trademind.user.dto;

public record MerchantCardDto(

        Long merchantId,

        String businessName,

        String businessEmail,

        String city,

        String state,

        String shopImageUrl

) {}
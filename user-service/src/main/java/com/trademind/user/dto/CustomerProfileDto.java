package com.trademind.user.dto;

public record CustomerProfileDto(
        Integer loyaltyPoints,
        Boolean newsletterSubscribed
) {}

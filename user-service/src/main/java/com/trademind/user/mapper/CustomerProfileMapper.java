package com.trademind.user.mapper;

import com.trademind.user.dto.CustomerProfileDto;
import com.trademind.user.entity.CustomerProfile;
import com.trademind.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CustomerProfileMapper {

    public CustomerProfileDto toDto(CustomerProfile entity) {
        if (entity == null) return null;

        return new CustomerProfileDto(
                entity.getLoyaltyPoints(),
                entity.getNewsletterSubscribed()
        );
    }

    public CustomerProfile toEntity(CustomerProfileDto dto, User user) {
        if (dto == null) return null;

        return CustomerProfile.builder()
                .user(user)
                .loyaltyPoints(dto.loyaltyPoints())
                .newsletterSubscribed(dto.newsletterSubscribed())
                .build();
    }
}

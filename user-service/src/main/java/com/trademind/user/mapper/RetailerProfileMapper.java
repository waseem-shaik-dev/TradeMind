package com.trademind.user.mapper;

import com.trademind.user.dto.RetailerProfileDto;
import com.trademind.user.entity.RetailerProfile;
import com.trademind.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RetailerProfileMapper {

    public RetailerProfileDto toDto(RetailerProfile entity) {
        if (entity == null) return null;

        return new RetailerProfileDto(
                entity.getShopName(),
                entity.getShopEmail()
        );
    }

    public RetailerProfile toEntity(RetailerProfileDto dto, User user) {
        if (dto == null) return null;

        return RetailerProfile.builder()
                .user(user)
                .shopName(dto.shopName())
                .shopEmail(dto.shopEmail())
                .build();
    }

}

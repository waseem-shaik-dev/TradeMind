package com.trademind.user.mapper;

import com.trademind.user.dto.RetailerProfileDto;
import com.trademind.user.entity.RetailerProfile;
import com.trademind.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetailerProfileMapper {

    private final StoreAddressMapper storeAddressMapper;

    public RetailerProfileDto toDto(RetailerProfile entity) {
        if (entity == null) return null;

        return new RetailerProfileDto(
                entity.getShopName(),
                entity.getShopEmail(),
                entity.getShopImageUrl(),
                storeAddressMapper.toDto(entity.getStoreAddress()) // ✅ ADDED
        );
    }

    public RetailerProfile toEntity(RetailerProfileDto dto, User user) {
        if (dto == null) return null;

        return RetailerProfile.builder()
                .user(user)
                .shopName(dto.shopName())
                .shopEmail(dto.shopEmail())
                .shopImageUrl(dto.shopImageUrl())
                .storeAddress(storeAddressMapper.toEntity(dto.storeAddress())) // ✅ ADDED
                .build();
    }
}

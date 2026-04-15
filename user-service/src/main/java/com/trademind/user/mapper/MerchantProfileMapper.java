package com.trademind.user.mapper;

import com.trademind.user.dto.MerchantProfileDto;
import com.trademind.user.entity.MerchantProfile;
import com.trademind.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MerchantProfileMapper {

    private final StoreAddressMapper storeAddressMapper;

    public MerchantProfileDto toDto(MerchantProfile entity) {
        if (entity == null) return null;

        return new MerchantProfileDto(
                entity.getBusinessName(),
                entity.getBusinessEmail(),
                entity.getShopImageUrl(),
                storeAddressMapper.toDto(entity.getStoreAddress())
        );
    }

    public MerchantProfile toEntity(MerchantProfileDto dto, User user) {
        if (dto == null) return null;

        return MerchantProfile.builder()
                .user(user)
                .businessName(dto.businessName())
                .businessEmail(dto.businessEmail())
                .shopImageUrl(dto.shopImageUrl())
                .storeAddress(storeAddressMapper.toEntity(dto.storeAddress()))
                .build();
    }
}

package com.trademind.user.mapper;

import com.trademind.user.dto.*;
import com.trademind.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserProfileMapper userProfileMapper;
    private final AddressMapper addressMapper;
    private final AdminProfileMapper adminProfileMapper;
    private final CustomerProfileMapper customerProfileMapper;
    private final MerchantProfileMapper merchantProfileMapper;
    private final RetailerProfileMapper retailerProfileMapper;

    public UserResponseDto toDto(User user) {
        if (user == null) return null;

        return new UserResponseDto(
                user.getId(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                userProfileMapper.toDto(user.getProfile()),
                mapAddresses(user),
                adminProfileMapper.toDto(user.getAdminProfile()),
                customerProfileMapper.toDto(user.getCustomerProfile()),
                merchantProfileMapper.toDto(user.getMerchantProfile()),
                retailerProfileMapper.toDto(user.getRetailerProfile())
        );
    }

    private List<AddressDto> mapAddresses(User user) {
        if (user.getAddresses() == null) return List.of();

        return user.getAddresses()
                .stream()
                .map(addressMapper::toDto)
                .toList();
    }
}

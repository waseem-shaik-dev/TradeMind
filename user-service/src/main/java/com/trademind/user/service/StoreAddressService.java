package com.trademind.user.service;

import com.trademind.user.dto.StoreAddressDto;

public interface StoreAddressService {

    StoreAddressDto getStoreAddress(Long userId);

    StoreAddressDto createStoreAddress(Long userId, StoreAddressDto dto);

    StoreAddressDto updateStoreAddress(Long userId, StoreAddressDto dto);

    void deleteStoreAddress(Long userId);
}
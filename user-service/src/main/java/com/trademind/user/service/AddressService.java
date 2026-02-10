package com.trademind.user.service;

import com.trademind.user.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getMyAddresses(Long userId);

    AddressDto getMyAddressById(Long userId, Long addressId);

    AddressDto createAddress(Long userId, AddressDto dto);

    AddressDto updateAddress(Long userId, Long addressId, AddressDto dto);

    void deleteAddress(Long userId, Long addressId);
}

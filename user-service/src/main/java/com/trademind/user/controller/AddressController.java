package com.trademind.user.controller;

import com.trademind.user.dto.AddressDto;
import com.trademind.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * GET all addresses of logged-in user
     */
    @GetMapping
    public List<AddressDto> getMyAddresses(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return addressService.getMyAddresses(userId);
    }

    /**
     * GET single address by id
     */
    @GetMapping("/{addressId}")
    public AddressDto getMyAddressById(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long addressId
    ) {
        return addressService.getMyAddressById(userId, addressId);
    }

    /**
     * CREATE address
     */
    @PostMapping
    public AddressDto createAddress(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody AddressDto dto
    ) {
        return addressService.createAddress(userId, dto);
    }

    /**
     * UPDATE address
     */
    @PutMapping("/{addressId}")
    public AddressDto updateAddress(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long addressId,
            @RequestBody AddressDto dto
    ) {
        return addressService.updateAddress(userId, addressId, dto);
    }

    /**
     * DELETE address
     */
    @DeleteMapping("/{addressId}")
    public void deleteAddress(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long addressId
    ) {
        addressService.deleteAddress(userId, addressId);
    }
}

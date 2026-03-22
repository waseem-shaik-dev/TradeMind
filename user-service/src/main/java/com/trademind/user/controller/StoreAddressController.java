package com.trademind.user.controller;

import com.trademind.user.dto.StoreAddressDto;
import com.trademind.user.service.StoreAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me/store-address")
@RequiredArgsConstructor
public class StoreAddressController {

    private final StoreAddressService storeAddressService;

    /**
     * GET store address
     */
    @GetMapping
    public StoreAddressDto getStoreAddress(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return storeAddressService.getStoreAddress(userId);
    }

    /**
     * CREATE store address
     */
    @PostMapping
    public StoreAddressDto createStoreAddress(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody StoreAddressDto dto
    ) {
        return storeAddressService.createStoreAddress(userId, dto);
    }

    /**
     * UPDATE store address
     */
    @PutMapping
    public StoreAddressDto updateStoreAddress(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody StoreAddressDto dto
    ) {
        return storeAddressService.updateStoreAddress(userId, dto);
    }

    /**
     * DELETE store address
     */
    @DeleteMapping
    public void deleteStoreAddress(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        storeAddressService.deleteStoreAddress(userId);
    }
}
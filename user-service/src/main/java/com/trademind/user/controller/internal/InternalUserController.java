package com.trademind.user.controller.internal;

import com.trademind.events.common.SellerSnapshotDto;
import com.trademind.user.dto.AddressDto;
import com.trademind.user.dto.UserResponseDto;
import com.trademind.user.dto.internal.CartSourceInfoResponse;
import com.trademind.user.service.AddressService;
import com.trademind.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/user")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;
    private final AddressService addressService;

    // Used by Cart Service
    @GetMapping("/cart-source")
    public SellerSnapshotDto getCartSourceInfo(
            @RequestParam Long sourceId,
            @RequestParam String sourceType) {

        return userService.getSellerSnapshot(sourceId, sourceType);
    }

    /**
     * GET all addresses of logged-in user
     */
    @GetMapping("/addresses")
    public List<AddressDto> getMyAddresses(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return addressService.getMyAddresses(userId);
    }

    /**
     * GET single address by id
     */
    @GetMapping("/addresses/{addressId}")
    public AddressDto getMyAddressById(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long addressId
    ) {
        return addressService.getMyAddressById(userId, addressId);
    }

    @GetMapping("/{userId}/detailed")
    public ResponseEntity<UserResponseDto> getFullUser(
            @PathVariable Long userId
    ) {
        UserResponseDto response = userService.getFullUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/business")
    public ResponseEntity<UserResponseDto> getBusinessUser(
            @PathVariable Long userId
    ) {
        UserResponseDto response = userService.getBusinessUser(userId);
        return ResponseEntity.ok(response);
    }

}

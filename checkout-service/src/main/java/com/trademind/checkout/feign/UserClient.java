package com.trademind.checkout.feign;

import com.trademind.checkout.feign.config.FeignClientConfig;
import com.trademind.checkout.feign.dto.user.AddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8082",
        configuration = FeignClientConfig.class
)
public interface UserClient {

    /**
     * Fetch single address of logged-in user.
     */
    @GetMapping("/api/users/me/addresses/{addressId}")
    AddressDto getMyAddressById(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable("addressId") Long addressId
    );

    /**
     * Fetch all addresses (for checkout page).
     */
    @GetMapping("/api/users/me/addresses")
    List<AddressDto> getMyAddresses(
            @RequestHeader("X-USER-ID") Long userId
    );
}

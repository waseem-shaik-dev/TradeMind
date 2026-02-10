package com.trademind.checkout.mapper;

import com.trademind.checkout.dto.response.CheckoutAddressResponseDto;
import com.trademind.checkout.entity.CheckoutAddressSnapshot;
import org.springframework.stereotype.Component;

@Component
public class CheckoutAddressMapper {

    public CheckoutAddressResponseDto toResponseDto(
            CheckoutAddressSnapshot address
    ) {
        if (address == null) {
            return null;
        }

        return new CheckoutAddressResponseDto(
                address.getFullName(),
                address.getPhone(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry()
        );
    }
}

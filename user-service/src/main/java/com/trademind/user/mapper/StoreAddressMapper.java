package com.trademind.user.mapper;

import com.trademind.user.dto.StoreAddressDto;
import com.trademind.user.entity.StoreAddress;
import org.springframework.stereotype.Component;

@Component
public class StoreAddressMapper {

    public StoreAddressDto toDto(StoreAddress address) {

        if (address == null) return null;

        return new StoreAddressDto(
                address.getId(),
                address.getLine1(),
                address.getLine2(),
                address.getCity(),
                address.getState(),
                address.getPincode(),
                address.getCountry(),
                address.getLatitude(),
                address.getLongitude(),
                address.getMapUrl()
        );
    }

    public StoreAddress toEntity(StoreAddressDto dto) {

        return StoreAddress.builder()
                .line1(dto.line1())
                .line2(dto.line2())
                .city(dto.city())
                .state(dto.state())
                .pincode(dto.pincode())
                .country(dto.country())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .mapUrl(dto.mapUrl())
                .build();
    }
}
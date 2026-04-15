package com.trademind.user.mapper;

import com.trademind.user.dto.AddressDto;
import com.trademind.user.entity.Address;
import com.trademind.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDto toDto(Address entity) {
        if (entity == null) return null;

        return new AddressDto(
                entity.getId(),
                entity.getFullName(),
                entity.getPhone(),
                entity.getLine1(),
                entity.getLine2(),
                entity.getCity(),
                entity.getState(),
                entity.getPincode(),
                entity.getCountry(),
                (entity.getIsPrimary()!=null)?entity.getIsPrimary():false
        );
    }

    public Address toEntity(AddressDto dto, User user) {
        if (dto == null) return null;

        return Address.builder()
                .id(dto.id())
                .fullName(dto.fullName())
                .phone(dto.phone())
                .line1(dto.line1())
                .line2(dto.line2())
                .city(dto.city())
                .state(dto.state())
                .pincode(dto.pincode())
                .country(dto.country())
                .user(user)
                .build();
    }
}

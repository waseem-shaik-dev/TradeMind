package com.trademind.user.serviceImpl;

import com.trademind.user.dto.AddressDto;
import com.trademind.user.entity.Address;
import com.trademind.user.entity.User;
import com.trademind.user.mapper.AddressMapper;
import com.trademind.user.repository.AddressRepository;
import com.trademind.user.repository.UserRepository;
import com.trademind.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    public List<AddressDto> getMyAddresses(Long userId) {
        return addressRepository.findByUser_Id(userId)
                .stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public AddressDto getMyAddressById(Long userId, Long addressId) {
        Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return addressMapper.toDto(address);
    }

    @Override
    public AddressDto createAddress(Long userId, AddressDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressMapper.toEntity(dto, user);
        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public AddressDto updateAddress(Long userId, Long addressId, AddressDto dto) {

        Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setFullName(dto.fullName());
        address.setPhone(dto.phone());
        address.setLine1(dto.line1());
        address.setLine2(dto.line2());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setPincode(dto.pincode());
        address.setCountry(dto.country());

        return addressMapper.toDto(address);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        addressRepository.deleteByIdAndUser_Id(addressId, userId);
    }
}

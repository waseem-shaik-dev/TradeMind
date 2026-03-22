package com.trademind.user.serviceImpl;

import com.trademind.user.dto.MerchantCardDto;
import com.trademind.user.dto.RetailerCardDto;
import com.trademind.user.entity.MerchantProfile;
import com.trademind.user.entity.RetailerProfile;
import com.trademind.user.entity.StoreAddress;
import com.trademind.user.repository.MerchantProfileRepository;
import com.trademind.user.repository.RetailerProfileRepository;
import com.trademind.user.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectoryServiceImpl implements DirectoryService {

    private final RetailerProfileRepository retailerRepo;
    private final MerchantProfileRepository merchantRepo;

    /* ===============================
       RETAILER DIRECTORY
       =============================== */

    @Override
    public Page<RetailerCardDto> getRetailers(
            int page,
            int size,
            String keyword) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("shopName").ascending());

        Page<RetailerProfile> retailers;

        if (keyword != null && !keyword.isBlank()) {
            retailers =
                    retailerRepo.findByShopNameContainingIgnoreCase(keyword, pageable);
        } else {
            retailers =
                    retailerRepo.findAll(pageable);
        }

        return retailers.map(this::mapRetailerCard);
    }

    /* ===============================
       MERCHANT DIRECTORY
       =============================== */

    @Override
    public Page<MerchantCardDto> getMerchants(
            int page,
            int size,
            String keyword) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("businessName").ascending());

        Page<MerchantProfile> merchants;

        if (keyword != null && !keyword.isBlank()) {
            merchants =
                    merchantRepo.findByBusinessNameContainingIgnoreCase(keyword, pageable);
        } else {
            merchants =
                    merchantRepo.findAll(pageable);
        }

        return merchants.map(this::mapMerchantCard);
    }

    /* ===============================
       MAPPERS
       =============================== */

    private RetailerCardDto mapRetailerCard(RetailerProfile retailer) {

        StoreAddress address = retailer.getStoreAddress();

        return new RetailerCardDto(
                retailer.getUser().getId(),
                retailer.getShopName(),
                retailer.getShopEmail(),
                address != null ? address.getCity() : null,
                address != null ? address.getState() : null,
                retailer.getShopImageUrl()
        );
    }

    private MerchantCardDto mapMerchantCard(MerchantProfile merchant) {

        StoreAddress address = merchant.getStoreAddress();

        return new MerchantCardDto(
                merchant.getUser().getId(),
                merchant.getBusinessName(),
                merchant.getBusinessEmail(),
                address != null ? address.getCity() : null,
                address != null ? address.getState() : null,
                merchant.getShopImageUrl()
        );
    }
}
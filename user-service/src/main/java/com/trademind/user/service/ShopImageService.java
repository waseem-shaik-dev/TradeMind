package com.trademind.user.service;

import org.springframework.web.multipart.MultipartFile;

public interface ShopImageService {

    String uploadRetailerShopImage(Long userId, MultipartFile file);

    String updateRetailerShopImage(Long userId, MultipartFile file);

    String uploadMerchantShopImage(Long userId, MultipartFile file);

    String updateMerchantShopImage(Long userId, MultipartFile file);

    void deleteRetailerShopImage(Long userId);

    void deleteMerchantShopImage(Long userId);
}
package com.trademind.user.service;

import org.springframework.web.multipart.MultipartFile;

public interface ShopImageService {

    String uploadRetailerShopImage(Long userId, MultipartFile file);

    String updateRetailerShopImage(Long userId, MultipartFile file);

    String uploadMerchantShopImage(Long userId, MultipartFile file);

    String updateMerchantShopImage(Long userId, MultipartFile file);

    void deleteRetailerShopImage(Long userId);

    void deleteMerchantShopImage(Long userId);

    String uploadRetailerShopImageByUrl(Long userId, String imageUrl);
    String updateRetailerShopImageByUrl(Long userId, String imageUrl);

    String uploadMerchantShopImageByUrl(Long userId, String imageUrl);
    String updateMerchantShopImageByUrl(Long userId, String imageUrl);
}
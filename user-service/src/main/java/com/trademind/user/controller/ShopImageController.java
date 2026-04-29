package com.trademind.user.controller;

import com.trademind.user.service.ShopImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/users/shop-image")
@RequiredArgsConstructor
public class ShopImageController {

    private final ShopImageService service;

    /* ===============================
       RETAILER SHOP IMAGE
       =============================== */

    @PostMapping("/retailer")
    public String uploadRetailerShopImage(
            @RequestParam Long userId,
            @RequestParam MultipartFile file) {

        return service.uploadRetailerShopImage(userId, file);
    }

    @PutMapping("/retailer")
    public String updateRetailerShopImage(
            @RequestParam Long userId,
            @RequestParam MultipartFile file) {

        return service.updateRetailerShopImage(userId, file);
    }

    @DeleteMapping("/retailer/{userId}")
    public void deleteRetailerShopImage(
            @PathVariable Long userId) {

        service.deleteRetailerShopImage(userId);
    }


    /* ===============================
       MERCHANT SHOP IMAGE
       =============================== */

    @PostMapping("/merchant")
    public String uploadMerchantShopImage(
            @RequestParam Long userId,
            @RequestParam MultipartFile file) {

        return service.uploadMerchantShopImage(userId, file);
    }

    @PutMapping("/merchant")
    public String updateMerchantShopImage(
            @RequestParam Long userId,
            @RequestParam MultipartFile file) {

        return service.updateMerchantShopImage(userId, file);
    }

    @DeleteMapping("/merchant/{userId}")
    public void deleteMerchantShopImage(
            @PathVariable Long userId) {

        service.deleteMerchantShopImage(userId);
    }


    /* ===============================
   RETAILER URL
   =============================== */

    @PostMapping("/retailer/url")
    public String uploadRetailerByUrl(
            @RequestParam Long userId,
            @RequestParam String imageUrl) {

        return service.uploadRetailerShopImageByUrl(userId, imageUrl);
    }

    @PutMapping("/retailer/url")
    public String updateRetailerByUrl(
            @RequestParam Long userId,
            @RequestParam String imageUrl) {

        return service.updateRetailerShopImageByUrl(userId, imageUrl);
    }

/* ===============================
   MERCHANT URL
   =============================== */

    @PostMapping("/merchant/url")
    public String uploadMerchantByUrl(
            @RequestParam Long userId,
            @RequestParam String imageUrl) {

        return service.uploadMerchantShopImageByUrl(userId, imageUrl);
    }

    @PutMapping("/merchant/url")
    public String updateMerchantByUrl(
            @RequestParam Long userId,
            @RequestParam String imageUrl) {

        return service.updateMerchantShopImageByUrl(userId, imageUrl);
    }

}
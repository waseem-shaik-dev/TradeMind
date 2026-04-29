package com.trademind.product.controller;

import com.trademind.product.dto.ProductImageResponse;
import com.trademind.product.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products/admin/{productId}/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService imageService;

    // ✅ SINGLE FILE UPLOAD (existing)
    @PostMapping
    public ProductImageResponse upload(
            @PathVariable Long productId,
            @RequestParam MultipartFile file,
            @RequestParam(defaultValue = "false") boolean primary) {

        return imageService.uploadImage(productId, file, primary);
    }

    // ✅ MULTIPLE FILE UPLOAD (existing)
    @PostMapping("/bulk")
    public List<ProductImageResponse> uploadMultipleImages(
            @PathVariable Long productId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(required = false) Integer primaryIndex
    ) {
        return imageService.uploadMultipleImages(productId, files, primaryIndex);
    }

    // 🔥 NEW: SINGLE URL UPLOAD
    @PostMapping("/url")
    public ProductImageResponse uploadByUrl(
            @PathVariable Long productId,
            @RequestParam String imageUrl,
            @RequestParam(defaultValue = "false") boolean primary) {

        return imageService.addImageByUrl(productId, imageUrl, primary);
    }

    // 🔥 NEW: MULTIPLE URL UPLOAD
    @PostMapping("/urls")
    public List<ProductImageResponse> uploadMultipleByUrl(
            @PathVariable Long productId,
            @RequestBody List<String> imageUrls,
            @RequestParam(required = false) Integer primaryIndex
    ) {
        return imageService.addMultipleImagesByUrl(productId, imageUrls, primaryIndex);
    }

    // ✅ GET IMAGES (existing)
    @GetMapping
    public List<ProductImageResponse> getProductImages(
            @PathVariable Long productId) {

        return imageService.getImagesByProductId(productId);
    }

    // 🔥 NEW: SET PRIMARY IMAGE
    @PutMapping("/{imageId}/primary")
    public ProductImageResponse setPrimary(
            @PathVariable Long imageId) {

        return imageService.updateImage(imageId, true);
    }

    // ✅ DELETE (existing but works for both types now)
    @DeleteMapping("/{imageId}")
    public void delete(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
    }
}
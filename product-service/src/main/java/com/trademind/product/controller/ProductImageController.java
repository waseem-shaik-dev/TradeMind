package com.trademind.product.controller;

import com.trademind.product.dto.ProductImageResponse;
import com.trademind.product.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products/{productId}/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService imageService;

    @PostMapping
    public ProductImageResponse upload(
            @PathVariable Long productId,
            @RequestParam MultipartFile file,
            @RequestParam(defaultValue = "false") boolean primary) {

        return imageService.uploadImage(productId, file, primary);
    }

    @PostMapping("/bulk")
    public List<ProductImageResponse> uploadMultipleImages(
            @PathVariable Long productId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(required = false) Integer primaryIndex
    ) {
        return imageService.uploadMultipleImages(productId, files, primaryIndex);
    }


    @GetMapping
    public List<ProductImageResponse> getProductImages(
            @PathVariable Long productId) {

        return imageService.getImagesByProductId(productId);
    }

    @DeleteMapping("/{imageId}")
    public void delete(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
    }
}

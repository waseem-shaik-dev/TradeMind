package com.trademind.product.service;

import com.trademind.product.dto.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    ProductImageResponse uploadImage(Long productId, MultipartFile file, boolean primary);

    List<ProductImageResponse> uploadMultipleImages(Long productId, List<MultipartFile> files, Integer primaryIndex);

    ProductImageResponse addImageByUrl(Long productId, String imageUrl, boolean primary);

    List<ProductImageResponse> addMultipleImagesByUrl(Long productId, List<String> imageUrls, Integer primaryIndex);

    void deleteImage(Long imageId);

    ProductImageResponse updateImage(Long imageId, boolean primary);

    List<ProductImageResponse> getImagesByProductId(Long productId);


}

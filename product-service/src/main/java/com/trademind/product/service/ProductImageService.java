package com.trademind.product.service;

import com.trademind.product.dto.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    ProductImageResponse uploadImage(
            Long productId,
            MultipartFile file,
            boolean primary
    );

    List<ProductImageResponse> uploadMultipleImages(
            Long productId,
            List<MultipartFile> files,
            Integer primaryIndex
    );

    List<ProductImageResponse> getImagesByProductId(Long productId);


    void deleteImage(Long imageId);


}

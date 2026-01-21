package com.trademind.product.service;

import com.trademind.product.dto.CloudinaryUploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    CloudinaryUploadResult uploadProductImage(MultipartFile file);

    void deleteImage(String publicId);
}

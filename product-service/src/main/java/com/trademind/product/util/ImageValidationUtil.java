package com.trademind.product.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ImageValidationUtil {

    private static final List<String> ALLOWED_EXT = List.of("jpg", "jpeg", "png", "webp");

    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String name = file.getOriginalFilename();
        if (name == null || !isValidExtension(name)) {
            throw new RuntimeException("Invalid file type");
        }
    }

    public void validateUrl(String url) {
        if (url == null || !url.startsWith("http")) {
            throw new RuntimeException("Invalid URL");
        }

        if (!isValidExtension(url)) {
            throw new RuntimeException("URL must be an image");
        }
    }

    private boolean isValidExtension(String name) {
        return ALLOWED_EXT.stream().anyMatch(name.toLowerCase()::endsWith);
    }
}
package com.trademind.product.serviceImpl;

import com.trademind.product.dto.ProductImageResponse;
import com.trademind.product.entity.Product;
import com.trademind.product.entity.ProductImage;
import com.trademind.product.mapper.ProductImageMapper;
import com.trademind.product.repository.ProductImageRepository;
import com.trademind.product.repository.ProductRepository;
import com.trademind.product.service.CloudinaryService;
import com.trademind.product.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final CloudinaryService cloudinaryService;
    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;
    private final ProductImageMapper imageMapper;

    @Override
    public ProductImageResponse uploadImage(
            Long productId,
            MultipartFile file,
            boolean primary) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (primary) {
            imageRepository.findByProductId(productId)
                    .forEach(img -> img.setPrimaryImage(false));
        }

        var uploadResult = cloudinaryService.uploadProductImage(file);

        ProductImage image = ProductImage.builder()
                .imageUrl(uploadResult.imageUrl())
                .publicId(uploadResult.publicId())
                .primaryImage(primary)
                .product(product)
                .build();

        return imageMapper.toResponse(imageRepository.save(image));
    }

    @Override
    public void deleteImage(Long imageId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        cloudinaryService.deleteImage(image.getPublicId());
        imageRepository.delete(image);
    }



    @Override
    public List<ProductImageResponse> getImagesByProductId(Long productId) {

        return imageMapper.toResponses(
                imageRepository.findByProductIdOrderByDisplayOrderAsc(productId)
        );
    }

    @Override
    public List<ProductImageResponse> uploadMultipleImages(
            Long productId,
            List<MultipartFile> files,
            Integer primaryIndex) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // If primaryIndex is provided, reset existing primary images
        if (primaryIndex != null) {
            imageRepository.findByProductId(productId)
                    .forEach(img -> img.setPrimaryImage(false));
        }

        List<ProductImage> savedImages = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            MultipartFile file = files.get(i);
            var uploadResult = cloudinaryService.uploadProductImage(file);

            boolean isPrimary =
                    primaryIndex != null && primaryIndex == i;

            ProductImage image = ProductImage.builder()
                    .imageUrl(uploadResult.imageUrl())
                    .publicId(uploadResult.publicId())
                    .primaryImage(isPrimary)
                    .displayOrder(i)
                    .product(product)
                    .build();

            savedImages.add(image);
        }

        imageRepository.saveAll(savedImages);

        return imageMapper.toResponses(savedImages);
    }

}

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

        boolean shouldBePrimary =
                primary || imageRepository.countByProductId(productId) == 0;


        if (shouldBePrimary) {
            imageRepository.clearPrimaryImages(productId);
        }


        var uploadResult = cloudinaryService.uploadProductImage(file);

        int nextOrder = (int) imageRepository.countByProductId(productId);

        ProductImage image = ProductImage.builder()
                .imageUrl(uploadResult.imageUrl())
                .publicId(uploadResult.publicId())
                .primaryImage(shouldBePrimary)
                .displayOrder(nextOrder)
                .build();

        product.addImage(image);

        productRepository.save(product);

        return imageMapper.toResponse(image);
    }

    @Override
    public void deleteImage(Long imageId) {

        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        Long productId = image.getProduct().getId();
        boolean wasPrimary = image.isPrimaryImage();

        cloudinaryService.deleteImage(image.getPublicId());
        imageRepository.delete(image);

        if (wasPrimary) {
            imageRepository.findFirstByProductIdOrderByDisplayOrderAsc(productId)
                    .ifPresent(img -> img.setPrimaryImage(true));
        }
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



        int startOrder = (int) imageRepository.countByProductId(productId);

        boolean shouldAutoAssignPrimary = (primaryIndex == null && startOrder == 0);

        if (primaryIndex != null || shouldAutoAssignPrimary) {
            imageRepository.clearPrimaryImages(productId);
        }

        for (int i = 0; i < files.size(); i++) {

            var uploadResult = cloudinaryService.uploadProductImage(files.get(i));

            boolean isPrimary =
                    primaryIndex != null
                            ? primaryIndex == i
                            : shouldAutoAssignPrimary && i == 0;

            ProductImage image = ProductImage.builder()
                    .imageUrl(uploadResult.imageUrl())
                    .publicId(uploadResult.publicId())
                    .primaryImage(isPrimary)
                    .displayOrder(startOrder + i)
                    .build();

            product.addImage(image);
        }

        productRepository.save(product);

        return imageMapper.toResponses(product.getImages());
    }


}

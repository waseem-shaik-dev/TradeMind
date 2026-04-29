package com.trademind.user.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.trademind.user.entity.MerchantProfile;
import com.trademind.user.entity.RetailerProfile;
import com.trademind.user.repository.MerchantProfileRepository;
import com.trademind.user.repository.RetailerProfileRepository;
import com.trademind.user.service.ShopImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopImageServiceImpl implements ShopImageService {

    private final Cloudinary cloudinary;
    private final RetailerProfileRepository retailerRepo;
    private final MerchantProfileRepository merchantRepo;

    @Override
    public String uploadRetailerShopImage(Long userId, MultipartFile file) {

        RetailerProfile retailer = retailerRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Retailer profile not found"));

        if (retailer.getShopImagePublicId() != null) {
            throw new RuntimeException("Shop image already exists. Use update.");
        }

        Map uploadResult = uploadToCloudinary(file);

        retailer.setShopImageUrl((String) uploadResult.get("secure_url"));
        retailer.setShopImagePublicId((String) uploadResult.get("public_id"));

        return retailer.getShopImageUrl();
    }

    @Override
    public String updateRetailerShopImage(Long userId, MultipartFile file) {

        RetailerProfile retailer = retailerRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Retailer profile not found"));

        if (retailer.getShopImagePublicId() != null) {
            deleteFromCloudinary(retailer.getShopImagePublicId());
        }

        Map uploadResult = uploadToCloudinary(file);

        retailer.setShopImageUrl((String) uploadResult.get("secure_url"));
        retailer.setShopImagePublicId((String) uploadResult.get("public_id"));

        return retailer.getShopImageUrl();
    }

    @Override
    public String uploadMerchantShopImage(Long userId, MultipartFile file) {

        MerchantProfile merchant = merchantRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Merchant profile not found"));

        if (merchant.getShopImagePublicId() != null) {
            throw new RuntimeException("Shop image already exists. Use update.");
        }

        Map uploadResult = uploadToCloudinary(file);

        merchant.setShopImageUrl((String) uploadResult.get("secure_url"));
        merchant.setShopImagePublicId((String) uploadResult.get("public_id"));

        return merchant.getShopImageUrl();
    }

    @Override
    public String updateMerchantShopImage(Long userId, MultipartFile file) {

        MerchantProfile merchant = merchantRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Merchant profile not found"));

        if (merchant.getShopImagePublicId() != null) {
            deleteFromCloudinary(merchant.getShopImagePublicId());
        }

        Map uploadResult = uploadToCloudinary(file);

        merchant.setShopImageUrl((String) uploadResult.get("secure_url"));
        merchant.setShopImagePublicId((String) uploadResult.get("public_id"));

        return merchant.getShopImageUrl();
    }

    @Override
    public void deleteRetailerShopImage(Long userId) {

        RetailerProfile retailer = retailerRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Retailer not found"));

        if (retailer.getShopImagePublicId() != null) {
            deleteFromCloudinary(retailer.getShopImagePublicId());
        }

        retailer.setShopImageUrl(null);
        retailer.setShopImagePublicId(null);
    }

    @Override
    public void deleteMerchantShopImage(Long userId) {

        MerchantProfile merchant = merchantRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Merchant not found"));

        if (merchant.getShopImagePublicId() != null) {
            deleteFromCloudinary(merchant.getShopImagePublicId());
        }

        merchant.setShopImageUrl(null);
        merchant.setShopImagePublicId(null);
    }

    /* ===================== */

    private Map uploadToCloudinary(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "trademind/shop-images",
                            "resource_type", "image"
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload shop image", e);
        }
    }

    private void deleteFromCloudinary(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }


    @Override
    public String uploadRetailerShopImageByUrl(Long userId, String imageUrl) {

        validateUrl(imageUrl);

        RetailerProfile retailer = retailerRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Retailer profile not found"));

        if (retailer.getShopImageUrl() != null) {
            throw new RuntimeException("Shop image already exists. Use update.");
        }

        retailer.setShopImageUrl(imageUrl);
        retailer.setShopImagePublicId(null);

        return retailer.getShopImageUrl();
    }

    @Override
    public String updateRetailerShopImageByUrl(Long userId, String imageUrl) {

        validateUrl(imageUrl);

        RetailerProfile retailer = retailerRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Retailer profile not found"));

        if (retailer.getShopImagePublicId() != null) {
            deleteFromCloudinary(retailer.getShopImagePublicId());
        }

        retailer.setShopImageUrl(imageUrl);
        retailer.setShopImagePublicId(null);

        return retailer.getShopImageUrl();
    }

    @Override
    public String uploadMerchantShopImageByUrl(Long userId, String imageUrl) {

        validateUrl(imageUrl);

        MerchantProfile merchant = merchantRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Merchant profile not found"));

        if (merchant.getShopImageUrl() != null) {
            throw new RuntimeException("Shop image already exists. Use update.");
        }

        merchant.setShopImageUrl(imageUrl);
        merchant.setShopImagePublicId(null);

        return merchant.getShopImageUrl();
    }

    @Override
    public String updateMerchantShopImageByUrl(Long userId, String imageUrl) {

        validateUrl(imageUrl);

        MerchantProfile merchant = merchantRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Merchant profile not found"));

        if (merchant.getShopImagePublicId() != null) {
            deleteFromCloudinary(merchant.getShopImagePublicId());
        }

        merchant.setShopImageUrl(imageUrl);
        merchant.setShopImagePublicId(null);

        return merchant.getShopImageUrl();
    }



    private void validateUrl(String url) {
        if (url == null || !url.startsWith("http")) {
            throw new RuntimeException("Invalid image URL");
        }
    }


}
package com.trademind.product.serviceImpl;

import com.trademind.product.dto.*;
import com.trademind.product.entity.*;
import com.trademind.product.repository.*;
import com.trademind.product.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MasterDataServiceImpl implements MasterDataService {

    private final BrandRepository brandRepository;
    private final ProductCategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitRepository;

    /* ---------------- CREATE ---------------- */

    @Override
    public IdNameResponse createBrand(CreateBrandRequest request) {

        if (brandRepository.existsByNameIgnoreCase(request.name())) {
            throw new RuntimeException("Brand already exists");
        }

        Brand brand = new Brand();
        brand.setName(request.name());

        Brand saved = brandRepository.save(brand);

        return new IdNameResponse(saved.getId(), saved.getName());
    }

    @Override
    public IdNameResponse createCategory(CreateCategoryRequest request) {

        ProductCategory category = new ProductCategory();
        category.setName(request.name());
        category.setParentCategoryId(request.parentCategoryId());

        ProductCategory saved = categoryRepository.save(category);

        return new IdNameResponse(saved.getId(), saved.getName());
    }

    @Override
    public IdNameResponse createUnit(CreateUnitRequest request) {

        if (unitRepository.existsByNameIgnoreCase(request.name())) {
            throw new RuntimeException("Unit already exists");
        }

        UnitOfMeasure unit = new UnitOfMeasure();
        unit.setName(request.name());

        UnitOfMeasure saved = unitRepository.save(unit);

        return new IdNameResponse(saved.getId(), saved.getName());
    }

    /* ---------------- GET ---------------- */

    @Override
    public List<IdNameResponse> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(b -> new IdNameResponse(b.getId(), b.getName()))
                .toList();
    }

    @Override
    public List<IdNameResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new IdNameResponse(c.getId(), c.getName()))
                .toList();
    }

    @Override
    public List<IdNameResponse> getAllUnits() {
        return unitRepository.findAll()
                .stream()
                .map(u -> new IdNameResponse(u.getId(), u.getName()))
                .toList();
    }
}

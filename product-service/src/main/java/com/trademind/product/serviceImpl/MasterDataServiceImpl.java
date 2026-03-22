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

    /* ---------------- CREATE BRAND ---------------- */

    @Override
    public IdNameResponse createBrand(CreateBrandRequest request) {

        if (brandRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Brand already exists");
        }

        Brand brand = new Brand();
        brand.setName(request.name());

        Brand saved = brandRepository.save(brand);

        return new IdNameResponse(saved.getId(), saved.getName());
    }

    /* ---------------- CREATE CATEGORY ---------------- */

    @Override
    public IdNameResponse createCategory(CreateCategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCaseAndParentCategoryId(
                request.name(),
                request.parentCategoryId()
        )) {
            throw new IllegalArgumentException(
                    "Category already exists under the same parent"
            );
        }

        if (request.parentCategoryId() != null &&
                !categoryRepository.existsById(request.parentCategoryId())) {
            throw new IllegalArgumentException("Parent category not found");
        }

        ProductCategory category = new ProductCategory();
        category.setName(request.name());
        category.setParentCategoryId(request.parentCategoryId());

        ProductCategory saved = categoryRepository.save(category);

        return new IdNameResponse(saved.getId(), saved.getName());
    }

    /* ---------------- CREATE UNIT ---------------- */

    @Override
    public IdNameResponse createUnit(CreateUnitRequest request) {

        if (unitRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Unit already exists");
        }

        UnitOfMeasure unit = new UnitOfMeasure();
        unit.setName(request.name());

        UnitOfMeasure saved = unitRepository.save(unit);

        return new IdNameResponse(saved.getId(), saved.getName());
    }

    /* ---------------- GET BRANDS ---------------- */

    @Override
    public List<IdNameResponse> getAllBrands() {

        return brandRepository.findAll()
                .stream()
                .map(b -> new IdNameResponse(b.getId(), b.getName()))
                .toList();
    }

    /* ---------------- GET CATEGORIES ---------------- */

    @Override
    public List<IdNameResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(c -> new IdNameResponse(c.getId(), c.getName()))
                .toList();
    }

    /* ---------------- GET UNITS ---------------- */

    @Override
    public List<IdNameResponse> getAllUnits() {

        return unitRepository.findAll()
                .stream()
                .map(u -> new IdNameResponse(u.getId(), u.getName()))
                .toList();
    }

    /* ---------------- ROOT CATEGORIES ---------------- */

    @Override
    public List<IdNameResponse> getRootCategories() {

        return categoryRepository.findByParentCategoryIdIsNull()
                .stream()
                .map(c -> new IdNameResponse(c.getId(), c.getName()))
                .toList();
    }

    /* ---------------- CHILD CATEGORIES ---------------- */

    @Override
    public List<IdNameResponse> getChildCategories(Long parentId) {

        return categoryRepository.findByParentCategoryId(parentId)
                .stream()
                .map(c -> new IdNameResponse(c.getId(), c.getName()))
                .toList();
    }

    @Override
    public List<IdNameResponse> createBrandsBulk(
            List<CreateBrandRequest> requests) {

        List<Brand> brands = requests.stream()
                .filter(r -> !brandRepository.existsByNameIgnoreCase(r.name()))
                .map(r -> {
                    Brand b = new Brand();
                    b.setName(r.name());
                    return b;
                })
                .toList();

        List<Brand> saved = brandRepository.saveAll(brands);

        return saved.stream()
                .map(b -> new IdNameResponse(b.getId(), b.getName()))
                .toList();
    }

    @Override
    public List<IdNameResponse> createUnitsBulk(
            List<CreateUnitRequest> requests) {

        List<UnitOfMeasure> units = requests.stream()
                .filter(r -> !unitRepository.existsByNameIgnoreCase(r.name()))
                .map(r -> {
                    UnitOfMeasure u = new UnitOfMeasure();
                    u.setName(r.name());
                    return u;
                })
                .toList();

        List<UnitOfMeasure> saved = unitRepository.saveAll(units);

        return saved.stream()
                .map(u -> new IdNameResponse(u.getId(), u.getName()))
                .toList();
    }

    @Override
    public List<IdNameResponse> createCategoriesBulk(
            List<CreateCategoryRequest> requests) {

        List<ProductCategory> categories = requests.stream()
                .map(r -> {

                    if (categoryRepository.existsByNameIgnoreCaseAndParentCategoryId(
                            r.name(),
                            r.parentCategoryId())) {
                        return null;
                    }

                    ProductCategory c = new ProductCategory();
                    c.setName(r.name());
                    c.setParentCategoryId(r.parentCategoryId());

                    return c;

                })
                .filter(c -> c != null)
                .toList();

        List<ProductCategory> saved = categoryRepository.saveAll(categories);

        return saved.stream()
                .map(c -> new IdNameResponse(c.getId(), c.getName()))
                .toList();
    }

}
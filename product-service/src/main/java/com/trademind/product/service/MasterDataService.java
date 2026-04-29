package com.trademind.product.service;

import com.trademind.product.dto.*;

import java.util.List;

public interface MasterDataService {

    IdNameResponse createBrand(CreateBrandRequest request);

    IdNameResponse createCategory(CreateCategoryRequest request);

    IdNameResponse createUnit(CreateUnitRequest request);

    List<IdNameResponse> getAllBrands();

    List<IdNameResponse> getAllCategories();

    List<IdNameResponse> getAllUnits();

    List<IdNameResponse> getRootCategories();

    List<IdNameResponse> getChildCategories(Long parentId);

    List<IdNameResponse> createBrandsBulk(
            List<CreateBrandRequest> requests);

    List<IdNameResponse> createUnitsBulk(
            List<CreateUnitRequest> requests);

    List<IdNameResponse> createCategoriesBulk(
            List<CreateCategoryRequest> requests);

    List<Long> getAllDescendantCategoryIds(Long parentId);
}

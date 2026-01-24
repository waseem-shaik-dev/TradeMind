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
}

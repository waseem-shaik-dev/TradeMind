package com.trademind.product.controller;

import com.trademind.product.dto.*;
import com.trademind.product.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/master")
@RequiredArgsConstructor
public class MasterDataController {

    private final MasterDataService masterDataService;

    /* -------- CREATE -------- */

    @PostMapping("/brands")
    public IdNameResponse createBrand(
            @RequestBody CreateBrandRequest request) {
        return masterDataService.createBrand(request);
    }

    @PostMapping("/categories")
    public IdNameResponse createCategory(
            @RequestBody CreateCategoryRequest request) {
        return masterDataService.createCategory(request);
    }

    @PostMapping("/units")
    public IdNameResponse createUnit(
            @RequestBody CreateUnitRequest request) {
        return masterDataService.createUnit(request);
    }

    /* -------- GET -------- */

    @GetMapping("/brands")
    public List<IdNameResponse> getBrands() {
        return masterDataService.getAllBrands();
    }

    @GetMapping("/categories")
    public List<IdNameResponse> getCategories() {
        return masterDataService.getAllCategories();
    }

    @GetMapping("/units")
    public List<IdNameResponse> getUnits() {
        return masterDataService.getAllUnits();
    }
}

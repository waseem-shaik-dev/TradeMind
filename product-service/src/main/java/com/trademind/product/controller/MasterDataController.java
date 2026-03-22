package com.trademind.product.controller;

import com.trademind.product.dto.*;
import com.trademind.product.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/admin/master")
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

    /* -------- GET ALL -------- */

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

    /* -------- CATEGORY SPECIAL -------- */

    @GetMapping("/categories/root")
    public List<IdNameResponse> getRootCategories() {
        return masterDataService.getRootCategories();
    }

    @GetMapping("/categories/{id}/children")
    public List<IdNameResponse> getChildCategories(
            @PathVariable Long id) {
        return masterDataService.getChildCategories(id);
    }

    @PostMapping("/brands/bulk")
    public List<IdNameResponse> createBrandsBulk(
            @RequestBody List<CreateBrandRequest> requests) {

        return masterDataService.createBrandsBulk(requests);
    }

    @PostMapping("/units/bulk")
    public List<IdNameResponse> createUnitsBulk(
            @RequestBody List<CreateUnitRequest> requests) {

        return masterDataService.createUnitsBulk(requests);
    }

    @PostMapping("/categories/bulk")
    public List<IdNameResponse> createCategoriesBulk(
            @RequestBody List<CreateCategoryRequest> requests) {

        return masterDataService.createCategoriesBulk(requests);
    }
}
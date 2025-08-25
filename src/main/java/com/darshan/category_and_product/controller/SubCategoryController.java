package com.darshan.category_and_product.controller;

import com.darshan.category_and_product.entity.SubCategory;
import com.darshan.category_and_product.services.SubCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    // Create subcategory under a category
    @PostMapping("/{categoryId}")
    public SubCategory createSubCategory(@PathVariable Long categoryId,
                                         @RequestBody SubCategory subCategory) {
        return subCategoryService.createSubCategory(categoryId, subCategory);
    }

    // Get all subcategories
    @GetMapping
    public List<SubCategory> getAllSubCategories() {
        return subCategoryService.getAllSubCategories();
    }
}

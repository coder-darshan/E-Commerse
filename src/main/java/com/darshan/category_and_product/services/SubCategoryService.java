package com.darshan.category_and_product.services;

import com.darshan.category_and_product.entity.SubCategory;
import com.darshan.category_and_product.entity.Category;
import com.darshan.category_and_product.repository.CategoryRepository;
import com.darshan.category_and_product.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepo;
    private final CategoryRepository categoryRepo;

    public SubCategoryService(SubCategoryRepository subCategoryRepo, CategoryRepository categoryRepo) {
        this.subCategoryRepo = subCategoryRepo;
        this.categoryRepo = categoryRepo;
    }

    public SubCategory createSubCategory(Long categoryId, SubCategory subCategory) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        subCategory.setCategory(category);
        return subCategoryRepo.save(subCategory);
    }

    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepo.findAll();
    }
}

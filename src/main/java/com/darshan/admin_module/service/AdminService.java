package com.darshan.admin_module.service;

import com.darshan.admin_module.entity.SubCategory;
import com.darshan.admin_module.repository.SubCategoryRepository;
import com.darshan.category_and_product.entity.Category;
import com.darshan.category_and_product.entity.Product;
import com.darshan.category_and_product.repository.CategoryRepository;
import com.darshan.category_and_product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    // Product CRUD
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        Product existing = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setCategory(product.getCategory());
        existing.setSubCategory(product.getSubCategory());
        return productRepository.save(existing);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Category CRUD
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public SubCategory addSubCategory(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<SubCategory> getSubCategories(Long categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    // Inventory overview
    public Integer getStockForProduct(Long productId) {
        Product product = getProduct(productId);
        return product.getStock();
    }
}

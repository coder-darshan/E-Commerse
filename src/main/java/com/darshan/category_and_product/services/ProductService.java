package com.darshan.category_and_product.services;

import com.darshan.category_and_product.entity.Product;
import com.darshan.category_and_product.repository.ProductRepository;
import com.darshan.search_filter.entity.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // ------------------ Create / Update ------------------
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // ------------------ Read ------------------
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> filterByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> filterByPriceRange(Double min, Double max) {
        return productRepository.findByPriceRange(min, max);
    }

    // ------------------ Delete ------------------
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    // ------------------ Advanced Search ------------------
    public Page<Product> searchProducts(
            String name,
            Long categoryId,
            Long subCategoryId,
            Double minPrice,
            Double maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return productRepository.findAll(
                ProductSpecification.getFilter(name, categoryId, subCategoryId, minPrice, maxPrice),
                pageRequest
        );
    }
}

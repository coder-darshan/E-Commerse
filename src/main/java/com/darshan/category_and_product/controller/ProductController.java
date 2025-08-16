package com.darshan.category_and_product.controller;
import com.darshan.category_and_product.entity.Product;
import com.darshan.category_and_product.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchByName(name);
    }

    @GetMapping("/filter/category/{categoryId}")
    public List<Product> filterByCategory(@PathVariable Long categoryId) {
        return productService.filterByCategory(categoryId);
    }

    @GetMapping("/filter/price")
    public List<Product> filterByPriceRange(@RequestParam Double min,
                                            @RequestParam Double max) {
        return productService.filterByPriceRange(min, max);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully!";
    }
}

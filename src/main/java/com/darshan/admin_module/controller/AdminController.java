package com.darshan.admin_module.controller;

import com.darshan.admin_panel.service.UserService;
import com.darshan.category_and_product.entity.SubCategory;
import com.darshan.admin_module.service.AdminService;
import com.darshan.auth.entity.User;
import com.darshan.category_and_product.entity.Category;
import com.darshan.category_and_product.entity.Product;
import com.darshan.category_and_product.services.ProductService;
import com.darshan.order_management.entity.Order;
import com.darshan.order_management.service.OrderService;
import com.darshan.reporting.entity.SalesReportDTO;
import com.darshan.reporting.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final ReportService reportService;
    private final AdminService adminService;

    // ------------------ User Management ------------------
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // ------------------ Product Management ------------------
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(adminService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getProduct(id));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(adminService.addProduct(product));
    }

    @PutMapping("/products")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        return ResponseEntity.ok(adminService.updateProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // ------------------ Inventory ------------------
    @GetMapping("/products/{id}/stock")
    public ResponseEntity<Integer> getStock(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getStockForProduct(id));
    }

    // ------------------ Category Management ------------------
    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return ResponseEntity.ok(adminService.addCategory(category));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(adminService.getAllCategories());
    }

    @PostMapping("/subcategories")
    public ResponseEntity<SubCategory> addSubCategory(@RequestBody SubCategory subCategory) {
        return ResponseEntity.ok(adminService.addSubCategory(subCategory));
    }

    @GetMapping("/subcategories/{categoryId}")
    public ResponseEntity<List<SubCategory>> getSubCategories(@PathVariable Long categoryId) {
        return ResponseEntity.ok(adminService.getSubCategories(categoryId));
    }

    // ------------------ Order Management ------------------
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }

    // ------------------ Analytics ------------------
    @GetMapping("/analytics/top-selling")
    public ResponseEntity<List<SalesReportDTO>> getTopSellingProducts(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return ResponseEntity.ok(reportService.getTopSellingProducts(start, end));
    }
}

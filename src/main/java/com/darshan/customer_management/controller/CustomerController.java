package com.darshan.customer_management.controller;

import com.darshan.customer_management.entity.Customer;
import com.darshan.customer_management.service.CustomerService;
import com.darshan.order_management.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // ---------- Customer Endpoints ----------

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/search")
    public Customer findByEmail(@RequestParam String email) {
        return customerService.getCustomerByEmail(email);
    }

    // ---------- Customer Order Endpoints ----------

    @PostMapping("/{customerId}/orders")
    public Order createOrderForCustomer(@PathVariable Long customerId, @RequestBody Order order) {
        return customerService.createOrderForCustomer(customerId, order);
    }

    @GetMapping("/{customerId}/orders")
    public List<Order> getOrdersByCustomer(@PathVariable Long customerId) {
        return customerService.getOrdersByCustomer(customerId);
    }
}

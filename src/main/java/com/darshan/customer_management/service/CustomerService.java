package com.darshan.customer_management.service;

import com.darshan.customer_management.entity.Customer;
import com.darshan.customer_management.repository.CustomerRepository;
import com.darshan.order_management.entity.Order;
import com.darshan.order_management.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    // Save customer
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Get customer by ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    // Get customer by email
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get all orders for a specific customer
    public List<Order> getOrdersByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        return customer.getOrders();
    }

    // Create and assign a new order for a customer
    public Order createOrderForCustomer(Long customerId, Order order) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        order.setCustomer(customer);
        return orderRepository.save(order);
    }
}

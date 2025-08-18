package com.darshan.order_management.entity;

import com.darshan.customer_management.entity.Customer;
import com.darshan.order_management.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders") // 'order' is reserved in SQL
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // e.g., PENDING, CONFIRMED, etc.

    // Optional: if you want to store a simple product name (not for multi-item orders)
    private String productName;

    private Double price;

    // If order has multiple items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    // Many orders belong to one customer
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

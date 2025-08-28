package com.darshan.cart.entity;

import com.darshan.auth.entity.User;
import com.darshan.customer_management.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One cart belongs to one user
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user; // Linked to authenticated user

    // One cart has many cart items
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>(); // Always initialize

    // One cart can also be linked to one customer (optional)
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

package com.darshan.cart.repository;
// CartItemRepository.java

import com.darshan.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

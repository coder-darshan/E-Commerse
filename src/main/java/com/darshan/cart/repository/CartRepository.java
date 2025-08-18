package com.darshan.cart.repository;
import com.darshan.auth.entity.User;
import com.darshan.cart.entity.Cart;
import com.darshan.customer_management.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    Optional<Cart> findByCustomer(Customer customer);

}

package com.darshan.wishlist.repository;

import com.darshan.customer_management.entity.Customer;
import com.darshan.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByCustomer(Customer customer);

    List<Wishlist> findByCustomerId(Long customerId);

}

package com.darshan.wishlist.service;

import com.darshan.category_and_product.entity.Product;
import com.darshan.category_and_product.repository.ProductRepository;
import com.darshan.customer_management.entity.Customer;
import com.darshan.customer_management.repository.CustomerRepository;
import com.darshan.wishlist.entity.Wishlist;
import com.darshan.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, CustomerRepository customerRepository,
                           ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public Wishlist getWishlist(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return wishlistRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setCustomer(customer);
                    return wishlistRepository.save(wishlist);
                });
    }

    public Wishlist addToWishlist(Long customerId, Long productId) {
        Wishlist wishlist = getWishlist(customerId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!wishlist.getProducts().contains(product)) {
            wishlist.getProducts().add(product);
        }

        return wishlistRepository.save(wishlist);
    }

    public Wishlist removeFromWishlist(Long customerId, Long productId) {
        Wishlist wishlist = getWishlist(customerId);
        wishlist.getProducts().removeIf(p -> p.getId().equals(productId));
        return wishlistRepository.save(wishlist);
    }
}

package com.darshan.cart.service;

import com.darshan.auth.entity.User;
import com.darshan.auth.repository.UserRepository;
import com.darshan.cart.entity.Cart;
import com.darshan.cart.entity.CartItem;
import com.darshan.cart.repository.CartItemRepository;
import com.darshan.cart.repository.CartRepository;
import com.darshan.category_and_product.entity.Product;
import com.darshan.category_and_product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).items(new ArrayList<>()).build()));

        // Check if product already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(cartItem);
        }

        return cartRepository.save(cart);
    }

    public Cart viewCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));
    }

    @Transactional
    public Cart removeItem(Long userId, Long itemId) {
        Cart cart = viewCart(userId);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartItemRepository.deleteById(itemId);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateQuantity(Long userId, Long itemId, int quantity) {
        Cart cart = viewCart(userId);
        cart.getItems().forEach(item -> {
            if (item.getId().equals(itemId)) {
                item.setQuantity(quantity);
            }
        });
        return cartRepository.save(cart);
    }

    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = viewCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}

package com.darshan.cart.controller;

import com.darshan.cart.entity.Cart;
import com.darshan.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Get cart by customer/user ID
    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        return cartService.viewCart(userId); // or getCartByCustomerId depending on service method
    }

    // Add product to cart (using user/customer ID)
    @PostMapping("/add")
    public Cart addToCart(@RequestParam Long userId,
                          @RequestParam Long productId,
                          @RequestParam int quantity) {
        return cartService.addToCart(userId, productId, quantity);
    }

    // Remove cart item by item ID and user/customer ID
    @DeleteMapping("/remove")
    public Cart removeItem(@RequestParam Long userId,
                           @RequestParam Long itemId) {
        return cartService.removeItem(userId, itemId);
    }

    // Update quantity of a cart item
    @PutMapping("/update")
    public Cart updateQuantity(@RequestParam Long userId,
                               @RequestParam Long itemId,
                               @RequestParam int quantity) {
        return cartService.updateQuantity(userId, itemId, quantity);
    }

    // Clear cart by user/customer ID
    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}

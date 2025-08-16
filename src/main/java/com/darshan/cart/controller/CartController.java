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

    @PostMapping("/add")
    public Cart addToCart(@RequestParam Long userId,
                          @RequestParam Long productId,
                          @RequestParam int quantity) {
        return cartService.addToCart(userId, productId, quantity);
    }

    @GetMapping("/{userId}")
    public Cart viewCart(@PathVariable Long userId) {
        return cartService.viewCart(userId);
    }

    @DeleteMapping("/remove")
    public Cart removeItem(@RequestParam Long userId,
                           @RequestParam Long itemId) {
        return cartService.removeItem(userId, itemId);
    }

    @PutMapping("/update")
    public Cart updateQuantity(@RequestParam Long userId,
                               @RequestParam Long itemId,
                               @RequestParam int quantity) {
        return cartService.updateQuantity(userId, itemId, quantity);
    }
}

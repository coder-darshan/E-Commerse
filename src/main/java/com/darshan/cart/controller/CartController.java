package com.darshan.cart.controller;

import com.darshan.cart.DTO.CartDTO;
import com.darshan.cart.entity.Cart;
import com.darshan.cart.mapper.CartMapper;
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
    public CartDTO getCart(@PathVariable Long userId) {
        Cart cart = cartService.viewCart(userId);
        return CartMapper.toDTO(cart); // or getCartByCustomerId depending on service method
    }

    // Add product to cart (using user/customer ID)
    @PostMapping("/add")
    public CartDTO  addToCart(@RequestParam Long userId,
                          @RequestParam Long productId,
                          @RequestParam int quantity) {
        Cart cart = cartService.addToCart(userId, productId, quantity);

        return CartMapper.toDTO(cart);
    }

    // Remove cart item by item ID and user/customer ID
    @DeleteMapping("/remove")
    public CartDTO removeItem(@RequestParam Long userId,
                              @RequestParam Long itemId) {
        Cart cart = cartService.removeItem(userId, itemId);
        return CartMapper.toDTO(cart);
    }

    // Update quantity of a cart item
    @PutMapping("/update")
    public CartDTO  updateQuantity(@RequestParam Long userId,
                               @RequestParam Long itemId,
                               @RequestParam int quantity) {

        Cart cart = cartService.updateQuantity(userId, itemId, quantity);
        return CartMapper.toDTO(cart);
    }

    // Clear cart by user/customer ID
    @DeleteMapping("/{userId}/clear")
    public CartDTO clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId);
    }
}

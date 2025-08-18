package com.darshan.wishlist.controller;

import com.darshan.wishlist.entity.Wishlist;
import com.darshan.wishlist.service.WishlistService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/{customerId}")
    public Wishlist getWishlist(@PathVariable Long customerId) {
        return wishlistService.getWishlist(customerId);
    }

    @PostMapping("/{customerId}/add")
    public Wishlist addToWishlist(@PathVariable Long customerId,
                                  @RequestParam Long productId) {
        return wishlistService.addToWishlist(customerId, productId);
    }

    @DeleteMapping("/{customerId}/remove")
    public Wishlist removeFromWishlist(@PathVariable Long customerId,
                                       @RequestParam Long productId) {
        return wishlistService.removeFromWishlist(customerId, productId);
    }
}

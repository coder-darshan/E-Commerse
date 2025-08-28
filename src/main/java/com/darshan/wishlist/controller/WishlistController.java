package com.darshan.wishlist.controller;

import com.darshan.wishlist.DTO.WishlistDTO;
import com.darshan.wishlist.entity.Wishlist;
import com.darshan.wishlist.mapper.WishlistMapper;
import com.darshan.wishlist.service.WishlistService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // Get Wishlist
    // GET http://localhost:8080/api/wishlist/1
    @GetMapping("/{customerId}")
    public WishlistDTO getWishlist(@PathVariable Long customerId) {
        Wishlist wishlist = wishlistService.getWishlist(customerId);
        return WishlistMapper.toDTO(wishlist);
    }

    // Add to Wishlist
    // POST http://localhost:8080/api/wishlist/1/add?productId=1
    @PostMapping("/{customerId}/add")
    public WishlistDTO addToWishlist(@PathVariable Long customerId,
                                     @RequestParam Long productId) {
        Wishlist wishlist = wishlistService.addToWishlist(customerId, productId);
        return WishlistMapper.toDTO(wishlist);
    }

    // Remove from Wishlist
    // DELETE http://localhost:8080/api/wishlist/1/remove?productId=1
    @DeleteMapping("/{customerId}/remove")
    public WishlistDTO removeFromWishlist(@PathVariable Long customerId,
                                          @RequestParam Long productId) {
        Wishlist wishlist = wishlistService.removeFromWishlist(customerId, productId);
        return WishlistMapper.toDTO(wishlist);
    }
}

package com.darshan.wishlist.mapper;

import com.darshan.wishlist.DTO.WishlistDTO;
import com.darshan.wishlist.entity.Wishlist;
import java.util.stream.Collectors;

public class WishlistMapper {

    public static WishlistDTO toDTO(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }

        return WishlistDTO.builder()
                .wishlistId(wishlist.getId())
                .customerId(wishlist.getCustomer().getId())
                .customerName(wishlist.getCustomer().getName())
                .products(
                        wishlist.getProducts().stream()
                                .map(product -> WishlistDTO.ProductDTO.builder()
                                        .id(product.getId())
                                        .name(product.getName())
                                        .price(product.getPrice())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }
}

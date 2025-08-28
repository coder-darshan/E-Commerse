package com.darshan.cart.mapper;

import com.darshan.cart.DTO.CartDTO;
import com.darshan.cart.DTO.CartItemDTO;
import com.darshan.cart.entity.Cart;
import com.darshan.cart.entity.CartItem;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartDTO toDTO(Cart cart) {
        return CartDTO.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .items(
                        cart.getItems().stream()
                                .map(CartMapper::toItemDTO)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static CartItemDTO toItemDTO(CartItem item) {
        return CartItemDTO.builder()
                .itemId(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName()) // assuming Product has getName()
                .quantity(item.getQuantity())
                .build();
    }
}

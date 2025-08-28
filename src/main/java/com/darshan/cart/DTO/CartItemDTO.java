package com.darshan.cart.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Long itemId;
    private Long productId;
    private String productName;
    private int quantity;
}

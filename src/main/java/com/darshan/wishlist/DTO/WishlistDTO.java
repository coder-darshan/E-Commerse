package com.darshan.wishlist.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDTO {
    private Long wishlistId;
    private Long customerId;
    private String customerName;
    private List<ProductDTO> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDTO {
        private Long id;
        private String name;
        private Double price;
    }
}


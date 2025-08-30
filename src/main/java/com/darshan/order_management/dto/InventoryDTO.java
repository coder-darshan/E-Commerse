package com.darshan.order_management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Data
public class InventoryDTO {
    private Long productId;
    private String productName;
    private Integer availableStock;
    private Integer reservedStock;
}

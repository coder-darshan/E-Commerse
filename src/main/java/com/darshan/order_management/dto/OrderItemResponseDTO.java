package com.darshan.order_management.dto;

import lombok.Data;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderItemResponseDTO {
    private String productName;
    private int quantity;
    private double price;
}

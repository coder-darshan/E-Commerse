package com.darshan.order_management.dto;

import lombok.Data;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderItemRequestDTO {
    private Long productId;
    private int quantity;
}

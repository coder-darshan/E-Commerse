package com.darshan.order_management.dto;

import lombok.Data;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderRequestDTO {
    private Long customerId;
    private List<OrderItemRequestDTO> items;
    private String paymentMethod;  // CARD, UPI, COD, etc.
}

package com.darshan.order_management.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderResponseDTO {
    private Long orderId;
    private Long customerId;
    private LocalDateTime orderDate;
    private Double totalPrice;
    private String status;   // PENDING, CONFIRMED, etc.
    private List<OrderItemResponseDTO> items;
    private PaymentResponseDTO payment;
}

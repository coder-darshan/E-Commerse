package com.darshan.order_management.dto;

import lombok.Data;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PaymentResponseDTO {
    private String method;
    private String status;
    private String transactionId;
    private Double amount;
    private String currency;
    private LocalDateTime paymentDate;
}

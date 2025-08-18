package com.darshan.order_management.entity;

import com.darshan.order_management.enums.PaymentMethod;
import com.darshan.order_management.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method; // CARD, UPI, COD, etc.

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // SUCCESS, FAILED, PENDING

    private String transactionId;   // From payment gateway like Stripe
    private String transactionRef;  // Optional ref: UPI ID / Razorpay order ID etc.

    private Double amount;
    private String currency;        // e.g., INR, USD

    private LocalDateTime paymentDate;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}

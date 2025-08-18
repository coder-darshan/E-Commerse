package com.darshan.order_management.service;

import com.darshan.order_management.entity.Order;
import com.darshan.order_management.entity.Payment;
import com.darshan.order_management.enums.PaymentMethod;
import com.darshan.order_management.enums.PaymentStatus;
import com.darshan.order_management.repository.OrderRepository;
import com.darshan.order_management.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // ----------- Initiate Payment with Validation Rules -----------
    public Payment initiatePayment(Long orderId, PaymentMethod method, Double amount, String currency) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Business rules
        if (method == PaymentMethod.COD && amount > 5000) {
            throw new IllegalArgumentException("COD not allowed for orders above 5000");
        }
        if (method == PaymentMethod.WALLET && amount > 1000) {
            throw new IllegalArgumentException("Wallet max limit is 1000 per transaction");
        }

        String mockTransactionId = "txn_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        Payment payment = Payment.builder()
                .order(order)
                .method(method)
                .status(PaymentStatus.PENDING)
                .amount(amount)
                .currency(currency)
                .transactionId(mockTransactionId)
                .paymentDate(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    // ----------- Complete Payment (simulate success) -----------
    public Payment completePayment(Long paymentId, String transactionRef) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setTransactionRef(transactionRef);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    // ----------- Fail Payment -----------
    public Payment failPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.FAILED);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    // ----------- Stripe Dummy Simulation (always SUCCESS) -----------
    public Payment processStripeDummyPayment(Long orderId, Double amount, String currency) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String mockTransactionId = "pi_" + UUID.randomUUID().toString().substring(0, 8);

        Payment payment = Payment.builder()
                .order(order)
                .method(PaymentMethod.UPI) // assume Stripe uses UPI/card internally
                .status(PaymentStatus.SUCCESS)
                .amount(amount)
                .currency(currency)
                .transactionId(mockTransactionId)
                .paymentDate(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    // ----------- Fetch Methods -----------
    public List<Payment> getPaymentsForOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }
}

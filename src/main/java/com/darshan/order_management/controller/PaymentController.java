package com.darshan.order_management.controller;

import com.darshan.order_management.entity.Payment;
import com.darshan.order_management.enums.PaymentMethod;
import com.darshan.order_management.enums.PaymentStatus;
import com.darshan.order_management.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 1) Initiate payment (Stripe simulation or COD/WALLET rules)
    @PostMapping("/initiate")
    public ResponseEntity<Payment> initiatePayment(@RequestParam Long orderId,
                                                   @RequestParam PaymentMethod method,
                                                   @RequestParam Double amount,
                                                   @RequestParam(defaultValue = "INR") String currency) {
        return ResponseEntity.ok(paymentService.initiatePayment(orderId, method, amount, currency));
    }

    // 2) Complete payment (simulate success)
    @PostMapping("/{paymentId}/complete")
    public ResponseEntity<Payment> completePayment(@PathVariable Long paymentId,
                                                   @RequestParam String transactionRef) {
        return ResponseEntity.ok(paymentService.completePayment(paymentId, transactionRef));
    }

    // 3) Mark payment as failed
    @PostMapping("/{paymentId}/fail")
    public ResponseEntity<Payment> failPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.failPayment(paymentId));
    }

    // 4) Simulate direct Stripe dummy payment (always SUCCESS)
    @PostMapping("/stripe")
    public ResponseEntity<Payment> simulateStripePayment(@RequestParam Long orderId,
                                                         @RequestParam Double amount,
                                                         @RequestParam(defaultValue = "INR") String currency) {
        return ResponseEntity.ok(paymentService.processStripeDummyPayment(orderId, amount, currency));
    }

    // 5) Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // 6) Get payments for a specific order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getPaymentsForOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentsForOrder(orderId));
    }

    // 7) Get payments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable String status) {
        try {
            PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(paymentService.getPaymentsByStatus(paymentStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

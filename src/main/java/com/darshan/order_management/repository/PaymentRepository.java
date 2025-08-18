package com.darshan.order_management.repository;

import com.darshan.order_management.entity.Payment;
import com.darshan.order_management.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderId(Long orderId);
//    List<Payment> findByStatus(String status);
List<Payment> findByStatus(PaymentStatus status);
}


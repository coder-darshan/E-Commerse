package com.darshan.order_management.repository;

import com.darshan.order_management.entity.OrderItem;
import com.darshan.reporting.entity.DailySalesReport;
import com.darshan.reporting.entity.SalesReportDTO;
import com.darshan.reporting.entity.TopSellingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Daily sales report
    @Query("SELECT new com.darshan.reporting.entity.DailySalesReport(" +
            "FUNCTION('DATE', oi.order.orderDate), " +
            "SUM(oi.price), COUNT(DISTINCT oi.order.id)) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.orderDate BETWEEN :start AND :end " +
            "GROUP BY FUNCTION('DATE', oi.order.orderDate) " +
            "ORDER BY FUNCTION('DATE', oi.order.orderDate) ASC")
    List<com.darshan.reporting.entity.DailySalesReport> getDailySales(LocalDate start, LocalDate end);

    // Top selling products (by quantity)
    @Query("SELECT new com.darshan.reporting.entity.TopSellingProduct(" +
            "oi.product.id, oi.product.name, SUM(oi.quantity)) " +
            "FROM OrderItem oi " +
            "GROUP BY oi.product.id, oi.product.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<com.darshan.reporting.entity.TopSellingProduct> getTopSellingProducts();

    // Top selling products within a date range
    @Query("SELECT new com.darshan.reporting.entity.SalesReportDTO(" +
            "oi.product.name, SUM(oi.quantity), SUM(oi.price)) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.product.id, oi.product.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<com.darshan.reporting.entity.SalesReportDTO> getTopSellingProductsByDateRange(
            LocalDateTime startDate, LocalDateTime endDate);
}



package com.darshan.order_management.repository;

import com.darshan.order_management.entity.OrderItem;
import com.darshan.reporting.entity.DailySalesReport;
import com.darshan.reporting.entity.SalesReportDTO;
import com.darshan.reporting.entity.TopSellingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // ✅ Daily sales report (use LocalDateTime)
    @Query("SELECT new com.darshan.reporting.entity.DailySalesReport(" +
            "CAST(oi.order.orderDate AS date), " +   // works in Hibernate 6+
            "SUM(oi.price), " +
            "COUNT(DISTINCT oi.order.id)) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.orderDate BETWEEN :start AND :end " +
            "GROUP BY CAST(oi.order.orderDate AS date) " +
            "ORDER BY CAST(oi.order.orderDate AS date) ASC")
    List<DailySalesReport> getDailySales(LocalDateTime start, LocalDateTime end);

    // ✅ Top selling products (overall)
    @Query("SELECT new com.darshan.reporting.entity.TopSellingProduct(" +
            "oi.product.id, " +
            "oi.product.name, " +
            "SUM(oi.quantity)) " +
            "FROM OrderItem oi " +
            "GROUP BY oi.product.id, oi.product.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<TopSellingProduct> getTopSellingProducts();

    // ✅ Top selling products (within a date range)
    @Query("SELECT new com.darshan.reporting.entity.SalesReportDTO(" +
            "oi.product.name, " +
            "SUM(oi.quantity), " +
            "SUM(oi.price)) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.product.id, oi.product.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<SalesReportDTO> getTopSellingProductsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}

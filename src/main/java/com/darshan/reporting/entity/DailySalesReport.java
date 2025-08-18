package com.darshan.reporting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Data
@Getter
@Setter
public class DailySalesReport {
    private LocalDate  date;      // yyyy-MM-dd
    private Double totalSales;
    private Long  totalOrders;

    // Constructor Hibernate will use
    public DailySalesReport(Date orderDate, Double totalSales, Long totalOrders) {
        this.totalOrders = totalOrders;
        this.totalSales = totalSales;
        this.totalOrders = totalOrders;
    }

}


//Repository for this is OrderItemRepository
package com.darshan.reporting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailySalesReport {
    private String date;      // yyyy-MM-dd
    private Double totalSales;
    private Integer totalOrders;
}


//Repository for this is OrderItemRepository
package com.darshan.reporting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesReportDTO {
    private String productName;
    private Long quantitySold;
    private double totalRevenue;

    // Constructor required by JPQL
    public SalesReportDTO(String productName, Long quantitySold, Double totalRevenue) {
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.totalRevenue = totalRevenue;
    }
}

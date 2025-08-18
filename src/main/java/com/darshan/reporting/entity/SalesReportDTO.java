package com.darshan.reporting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesReportDTO {
    private String productName;
    private int quantitySold;
    private double totalRevenue;
}

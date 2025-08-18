package com.darshan.reporting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

import com.darshan.reporting.entity.DailySalesReport;
import com.darshan.reporting.entity.TopSellingProduct;
import com.darshan.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    // Daily sales report
    @GetMapping("/sales/daily")
    public List<DailySalesReport> getDailySales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return reportingService.getDailySales(start, end);
    }

    // Top-selling products
    @GetMapping("/products/top")
    public List<TopSellingProduct> getTopSellingProducts() {
        return reportingService.getTopSellingProducts();
    }
    // Export Excel
    @GetMapping("/sales/daily/excel")
    public ResponseEntity<byte[]> exportDailySalesExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) throws Exception {
        ByteArrayInputStream in = reportingService.exportDailySalesToExcel(start, end);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=daily_sales.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(in.readAllBytes());
    }

    // Export PDF
    @GetMapping("/sales/daily/pdf")
    public ResponseEntity<byte[]> exportDailySalesPDF(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) throws Exception {
        ByteArrayInputStream in = reportingService.exportDailySalesToPDF(start, end);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=daily_sales.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(in.readAllBytes());
    }

    // Export Top-Selling Products to Excel
    @GetMapping("/products/top/excel")
    public ResponseEntity<byte[]> exportTopSellingProductsExcel() throws Exception {
        ByteArrayInputStream in = reportingService.exportTopSellingProductsToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=top_selling_products.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(in.readAllBytes());
    }

    // Export Top-Selling Products to PDF
    @GetMapping("/products/top/pdf")
    public ResponseEntity<byte[]> exportTopSellingProductsPDF() throws Exception {
        ByteArrayInputStream in = reportingService.exportTopSellingProductsToPDF();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=top_selling_products.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(in.readAllBytes());
    }
}

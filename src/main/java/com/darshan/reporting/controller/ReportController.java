package com.darshan.reporting.controller;

import com.darshan.reporting.entity.SalesReportDTO;
import com.darshan.reporting.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // Get Top-selling products between dates
    @GetMapping("/top-selling")
    public List<SalesReportDTO> getTopSelling(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return reportService.getTopSellingProducts(start, end);
    }

    // Export Excel
    @GetMapping("/top-selling/excel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<SalesReportDTO> reportList = reportService.getTopSellingProducts(start, end);
        ByteArrayInputStream in = reportService.exportExcel(reportList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SalesReport.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(in.readAllBytes());
    }

    // Export PDF
    @GetMapping("/top-selling/pdf")
    public ResponseEntity<byte[]> exportPDF(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<SalesReportDTO> reportList = reportService.getTopSellingProducts(start, end);
        ByteArrayInputStream in = reportService.exportPDF(reportList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SalesReport.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(in.readAllBytes());
    }
}

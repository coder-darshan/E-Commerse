package com.darshan.reporting.service;


import com.darshan.order_management.repository.OrderItemRepository;
import com.darshan.reporting.entity.DailySalesReport;
import com.darshan.reporting.entity.TopSellingProduct;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final OrderItemRepository orderItemRepository;

    // Get daily sales between dates
    public List<DailySalesReport> getDailySales(LocalDate start, LocalDate end) {
        return orderItemRepository.getDailySales(start, end);
    }

    // Get top-selling products
    public List<TopSellingProduct> getTopSellingProducts() {
        return orderItemRepository.getTopSellingProducts();
    }
    // Export daily sales report to Excel
    public ByteArrayInputStream exportDailySalesToExcel(LocalDate start, LocalDate end) throws Exception {
        List<DailySalesReport> reports = getDailySales(start, end);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Daily Sales");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Total Sales");
            header.createCell(2).setCellValue("Total Orders");

            int rowIdx = 1;
            for (DailySalesReport r : reports) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.getDate());
                row.createCell(1).setCellValue(r.getTotalSales());
                row.createCell(2).setCellValue(r.getTotalOrders());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // Export daily sales report to PDF
    public ByteArrayInputStream exportDailySalesToPDF(LocalDate start, LocalDate end) throws Exception {
        List<DailySalesReport> reports = getDailySales(start, end);
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(14);
        Paragraph title = new Paragraph("Daily Sales Report", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // empty line

        PdfPTable table = new PdfPTable(3);
        table.addCell("Date");
        table.addCell("Total Sales");
        table.addCell("Total Orders");

        for (DailySalesReport r : reports) {
            table.addCell(r.getDate());
            table.addCell(String.valueOf(r.getTotalSales()));
            table.addCell(String.valueOf(r.getTotalOrders()));
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Export Top-Selling Products to Excel
    public ByteArrayInputStream exportTopSellingProductsToExcel() throws Exception {
        List<TopSellingProduct> products = getTopSellingProducts();
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Top Selling Products");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Product ID");
            header.createCell(1).setCellValue("Product Name");
            header.createCell(2).setCellValue("Total Quantity Sold");

            int rowIdx = 1;
            for (TopSellingProduct p : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getProductId());
                row.createCell(1).setCellValue(p.getProductName());
                row.createCell(2).setCellValue(p.getTotalQuantitySold());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // Export Top-Selling Products to PDF
    public ByteArrayInputStream exportTopSellingProductsToPDF() throws Exception {
        List<TopSellingProduct> products = getTopSellingProducts();
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(14);
        Paragraph title = new Paragraph("Top-Selling Products", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // empty line

        PdfPTable table = new PdfPTable(3);
        table.addCell("Product ID");
        table.addCell("Product Name");
        table.addCell("Total Quantity Sold");

        for (TopSellingProduct p : products) {
            table.addCell(String.valueOf(p.getProductId()));
            table.addCell(p.getProductName());
            table.addCell(String.valueOf(p.getTotalQuantitySold()));
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}


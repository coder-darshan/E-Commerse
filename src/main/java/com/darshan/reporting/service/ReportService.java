package com.darshan.reporting.service;

import com.darshan.order_management.repository.OrderItemRepository;
import com.darshan.reporting.entity.SalesReportDTO;
import com.lowagie.text.Font;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.awt.Color;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderItemRepository orderItemRepository;

    // Fetch top-selling products
    // Fetch top-selling products for a date range
    public List<SalesReportDTO> getTopSellingProducts(LocalDateTime startDate, LocalDateTime endDate) {
        return orderItemRepository.getTopSellingProductsByDateRange(startDate, endDate);
    }

    // Export Excel
    public ByteArrayInputStream exportExcel(List<SalesReportDTO> reportList) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Sales Report");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Product Name");
            header.createCell(1).setCellValue("Quantity Sold");
            header.createCell(2).setCellValue("Total Revenue");

            int rowIdx = 1;
            for (SalesReportDTO dto : reportList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getProductName());
                row.createCell(1).setCellValue(dto.getQuantitySold());
                row.createCell(2).setCellValue(dto.getTotalRevenue());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to export Excel: " + e.getMessage());
        }
    }

    // Export PDF
    public ByteArrayInputStream exportPDF(List<SalesReportDTO> reportList) {
        Document document = new Document();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(14);
            Paragraph title = new Paragraph("Sales Report", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 2, 2});

            Stream.of("Product Name", "Quantity Sold", "Total Revenue")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(Color.LIGHT_GRAY);
                        header.setPhrase(new Phrase(headerTitle));
                        table.addCell(header);
                    });

            for (SalesReportDTO dto : reportList) {
                table.addCell(dto.getProductName());
                table.addCell(String.valueOf(dto.getQuantitySold()));
                table.addCell(String.valueOf(dto.getTotalRevenue()));
            }

            document.add(table);
            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to export PDF: " + e.getMessage());
        }
    }
}

package com.example.desktime.util;

import com.example.desktime.responseDTO.DailyActivityReportResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {

    public ByteArrayInputStream generateExcel(List<DailyActivityReportResponse> reportData) throws IOException {
        String[] columns = {"ID", "User Name", "User Email", "Login Time", "Logout Time", "Date", "Present", "Total Time"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Monthly Report");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create header row
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Create data rows
            int rowNum = 1;
            for (DailyActivityReportResponse report : reportData) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(report.getId());
                row.createCell(1).setCellValue(report.getUserName());
                row.createCell(2).setCellValue(report.getUserEmail());
                row.createCell(3).setCellValue(report.getLoginTime() != null ? report.getLoginTime().toString() : "N/A");
                row.createCell(4).setCellValue(report.getLogoutTime() != null ? report.getLogoutTime().toString() : "N/A");
                row.createCell(5).setCellValue(report.getDate().toString());
                row.createCell(6).setCellValue(report.getPresent());
                row.createCell(7).setCellValue(report.getTotalTime());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }

    }
}

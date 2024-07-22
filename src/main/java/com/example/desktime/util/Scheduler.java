package com.example.desktime.util;

import com.example.desktime.model.DailyActivity;
import com.example.desktime.repository.DailyActivityRepository;
import com.example.desktime.responseDTO.DailyActivityReportResponse;
import com.example.desktime.service.DailyActivityService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class Scheduler {

    @Autowired
    private DailyActivityRepository dailyActivityRepository  ;

    @Autowired
    private DailyActivityService dailyActivityService;


    @Scheduled(cron = "0 0 15 * * *")
    private void removeDuplicateDailyActivity() {
        List<DailyActivity> dailyActivityList = dailyActivityRepository.findDuplicateEntryInActivity();

        if (dailyActivityList.size() > 0) {
            for (DailyActivity dailyActivity : dailyActivityList) {
                dailyActivityRepository.delete(dailyActivity);
            }
        }
    }

//    @Scheduled(fixedRate = 2000)
    @Scheduled(cron = "0 0 0 1 * *")  // Runs at midnight on the first day of each month
    public ResponseEntity<?> autoGetAllUserMonthlyReport() {
        try {
            // Get current year and month
            LocalDate now = LocalDate.now();

            // Get the previous month
            YearMonth previousMonth = YearMonth.from(now.minusMonths(1));
            LocalDate startDate = previousMonth.atDay(1);
            LocalDate endDate = previousMonth.atEndOfMonth();

            // Fetch the data
            List<DailyActivityReportResponse> response = dailyActivityService.getAllUserMonthlyReport(startDate, endDate);

            if (response == null || response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No activity found for the specified period.");
            }

//            System.out.println("Auto working ");
            // Create Excel file
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("User Monthly Report");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headersArray = {"ID", "User Name", "User Email", "Date", "Login Time", "Logout Time", "Present", "Total Time", "Day of Week", "Attendance Type"};
            for (int i = 0; i < headersArray.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headersArray[i]);
            }

            // Populate rows
            int rowNum = 1;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            for (DailyActivityReportResponse activity : response) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(activity.getId());
                row.createCell(1).setCellValue(activity.getUserName());
                row.createCell(2).setCellValue(activity.getUserEmail());
                row.createCell(3).setCellValue(activity.getDate().format(dateFormatter));
                row.createCell(4).setCellValue(activity.getLoginTime() != null ? activity.getLoginTime().format(timeFormatter) : "N/A");
                row.createCell(5).setCellValue(activity.getLogoutTime() != null ? activity.getLogoutTime().format(timeFormatter) : "N/A");
                row.createCell(6).setCellValue(activity.getPresent());
                row.createCell(7).setCellValue(activity.getTotalTime());
                row.createCell(8).setCellValue(activity.getDayOfWeek());
                row.createCell(9).setCellValue(activity.getAttendanceType());
            }

            workbook.write(out);
            workbook.close();

            // Generate file name based on previous month and year
            String fileName = previousMonth.format(DateTimeFormatter.ofPattern("MMMM_yyyy")) + ".xlsx";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request: " + e.getMessage());
        }
    }
}

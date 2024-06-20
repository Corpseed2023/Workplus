package com.example.desktime.controller;

import com.example.desktime.repository.DailyActivityRepository;
import com.example.desktime.requestDTO.DailyActivityRequest;
import com.example.desktime.requestDTO.EditDailyActivityRequest;
import com.example.desktime.requestDTO.LogoutUpdateRequest;
import com.example.desktime.responseDTO.DailyActivityReportResponse;
import com.example.desktime.responseDTO.DailyActivityResponse;
import com.example.desktime.responseDTO.LogoutUpdateResponse;
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
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class DailyActivityController {

    @Autowired
    private DailyActivityService dailyActivityService;

    @Autowired
    private DailyActivityRepository dailyActivityRepository;


    @PostMapping("/saveDailyActivity")
    public ResponseEntity<?> saveDailyActivity(@RequestBody DailyActivityRequest request) {
        try {

//            System.out.println("Get Hit By Process");
            if (request.getEmail() == null || !request.getEmail().endsWith("@corpseed.com")) {
                return new ResponseEntity<>("User not found within the domain corpseed.com. Email is null or doesn't contain the specified domain.", HttpStatus.NOT_FOUND);
            }


            // No need to parse LocalDate again, it's already a LocalDate object
            DailyActivityResponse response = dailyActivityService.saveDailyActivity(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format. Please provide the date in yyyy-MM-dd format.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping("/updateLogoutTime")
    public ResponseEntity<?> updateLogoutTime(@RequestBody LogoutUpdateRequest request) {
//        System.out.println("Error Test 1123");

        try {

            LogoutUpdateResponse response = dailyActivityService.updateLogoutTime(request);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dailyActivity")
    public ResponseEntity<?> getDailyActivity(@RequestParam String email , @RequestParam(required = false) LocalDate date) {
        try {
            LocalDate currentDate;
            if (date != null) {
                currentDate = date;
            } else {
                currentDate = LocalDate.now();
            }

            DailyActivityResponse dailyActivityResponse = dailyActivityService.getDailyActivityByEmail(email, currentDate);
            return new ResponseEntity<>(dailyActivityResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filteredDailyActivityData")
    public ResponseEntity<?> getFilteredDailyActivityData(@RequestParam String email ,@RequestParam LocalDate date) {
        try {

            DailyActivityResponse dailyActivityResponse = dailyActivityService.getDailyActivityByEmail(email,date);
            return new ResponseEntity<>(dailyActivityResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/report")
////    public ResponseEntity<?> getMonthlyActivityReport(@RequestParam String email, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//    public ResponseEntity<?> getMonthlyActivityReport(@RequestParam String email, @RequestParam(required = false) LocalDate date) {
//
//        try {
//            LocalDate currentDate = (date != null) ? date : LocalDate.now();
//            int year = currentDate.getYear();
//            int month = currentDate.getMonthValue();
//            LocalDate startDate = LocalDate.of(year, month, 1);
//            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
//            List<DailyActivityReportResponse> response = dailyActivityService.getMonthlyActivityReport(email, startDate, endDate);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
//        }
//    }

    //User Wise Report Fetch
    @GetMapping("/report")
    public ResponseEntity<?> getMonthlyActivityReport(@RequestParam String email, @RequestParam int year, @RequestParam int month) {



        try {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            List<DailyActivityReportResponse> response = dailyActivityService.getMonthlyActivityReport(email, startDate, endDate);


            if (response == null || response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No activity found for the specified period.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request: " + e.getMessage());
        }
    }

    //Fetch all user Report accordingly Year And Month
    @GetMapping("/allUserMonthlyReport")
    public ResponseEntity<?> getAllUserMonthlyReport(@RequestParam int year, @RequestParam int month) {

        try {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            List<DailyActivityReportResponse> response = dailyActivityService.getAllUserMonthlyReport(startDate, endDate);

            if (response == null || response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No activity found for the specified period.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request: " + e.getMessage());
        }
    }

    @PutMapping("/editDailyActivity")
    public ResponseEntity<?> editDailyActivity(@RequestBody EditDailyActivityRequest request,@RequestParam Long userId) {
        try {
            DailyActivityResponse response = dailyActivityService.editDailyActivity(request ,userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/autoAllUserMonthlyReport")
    public ResponseEntity<?> autoGetAllUserMonthlyReport(@RequestParam int year, @RequestParam int month) {
        try {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            List<DailyActivityReportResponse> response = dailyActivityService.getAllUserMonthlyReport(startDate, endDate);

            if (response == null || response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No activity found for the specified period.");
            }

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
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attachment; filename=user_monthly_report.xlsx");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request: " + e.getMessage());
        }
    }




}

//package com.example.desktime.scheduler;
//
//
//import com.example.desktime.responseDTO.DailyActivityReportResponse;
//import com.example.desktime.service.DailyActivityService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class DailyReportScheduler {
//
//    @Autowired
//    private DailyActivityService dailyActivityService;
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    // Schedule the task to run daily at 8:00 PM
//    @Scheduled(cron = "0 0 20 * * ?")
//    public void sendMonthlyActivityReport() {
//        LocalDate now = LocalDate.now();
//        LocalDate startDate = now.withDayOfMonth(1);
//        LocalDate endDate = now;
//
//        // Fetch all users
//        List<String> userEmails = dailyActivityService.getAllUserEmails();
//
//        for (String email : userEmails) {
//            List<DailyActivityReportResponse> reportData = dailyActivityService.getMonthlyActivityReport(email, startDate, endDate);
//            String report = generateReport(reportData);
//            sendEmail(email, report);
//        }
//    }
//
//    private String generateReport(List<DailyActivityReportResponse> reportData) {
//        StringBuilder report = new StringBuilder();
//        report.append("Daily Activity Report:\n\n");
//        for (DailyActivityReportResponse data : reportData) {
//            report.append("Date: ").append(data.getDate()).append("\n");
//            report.append("Login Time: ").append(data.getLoginTime()).append("\n");
//            report.append("Logout Time: ").append(data.getLogoutTime()).append("\n");
//            report.append("Total Time: ").append(data.getTotalTime()).append("\n");
//            report.append("Status: ").append(data.isPresent() ? "Present" : "Absent").append("\n\n");
//        }
//        return report.toString();
//    }
//
//    private void sendEmail(String to, String report) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("Monthly Activity Report");
//        message.setText(report);
//        javaMailSender.send(message);
//    }
//
//}
//
//

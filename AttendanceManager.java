import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class AttendanceManager {
    // Collections to store data
    private List<Student> students;
    private List<AttendanceRecord> attendanceRecords;
    private Map<String, Student> studentMap; // For quick lookup
    
    public AttendanceManager() {
        this.students = FileHandler.loadStudents();
        this.attendanceRecords = FileHandler.loadAttendance();
        this.studentMap = new HashMap<>();
        
        // Populate the map for quick student lookup
        for (Student student : students) {
            studentMap.put(student.getStudentId(), student);
        }
    }
    
    // Mark attendance for multiple students
    public void markAttendance(Set<String> presentStudentIds) {
        List<AttendanceRecord> newRecords = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        // Check if attendance already marked for today
        boolean attendanceMarkedToday = attendanceRecords.stream()
            .anyMatch(record -> record.getDate().equals(today));
            
        if (attendanceMarkedToday) {
            System.out.println("Attendance already marked for today!");
            return;
        }
        
        // Create records for all students
        for (Student student : students) {
            String status = presentStudentIds.contains(student.getStudentId()) ? "Present" : "Absent";
            newRecords.add(new AttendanceRecord(student.getStudentId(), status));
        }
        
        // Save to file and update collection
        FileHandler.saveAttendance(newRecords);
        attendanceRecords.addAll(newRecords);
        System.out.println("Attendance marked for " + presentStudentIds.size() + " students.");
    }
    
    // View daily report
    public void viewDailyReport(LocalDate date) {
        System.out.println("\n=== Daily Attendance Report for " + date + " ===");
        
        List<AttendanceRecord> dailyRecords = attendanceRecords.stream()
            .filter(record -> record.getDate().equals(date))
            .collect(Collectors.toList());
            
        if (dailyRecords.isEmpty()) {
            System.out.println("No attendance records found for this date.");
            return;
        }
        
        int presentCount = 0;
        int totalCount = dailyRecords.size();
        
        for (AttendanceRecord record : dailyRecords) {
            Student student = studentMap.get(record.getStudentId());
            String studentName = (student != null) ? student.getName() : "Unknown";
            System.out.printf("ID: %s, Name: %-15s, Status: %s%n", 
                record.getStudentId(), studentName, record.getStatus());
            
            if ("Present".equals(record.getStatus())) {
                presentCount++;
            }
        }
        
        double attendancePercentage = (double) presentCount / totalCount * 100;
        System.out.printf("%nSummary: %d/%d students present (%.2f%%)%n", 
            presentCount, totalCount, attendancePercentage);
    }
    
    // View monthly report
    public void viewMonthlyReport(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        System.out.println("\n=== Monthly Attendance Report for " + yearMonth + " ===");
        
        Map<String, List<AttendanceRecord>> monthlyRecordsByStudent = attendanceRecords.stream()
            .filter(record -> YearMonth.from(record.getDate()).equals(yearMonth))
            .collect(Collectors.groupingBy(AttendanceRecord::getStudentId));
            
        if (monthlyRecordsByStudent.isEmpty()) {
            System.out.println("No attendance records found for this month.");
            return;
        }
        
        // Calculate working days in month
        int workingDays = calculateWorkingDays(year, month);
        
        System.out.printf("%-10s %-15s %-10s %-10s%n", 
            "Student ID", "Name", "Present", "Percentage");
        System.out.println("=" .repeat(50));
        
        for (Map.Entry<String, List<AttendanceRecord>> entry : monthlyRecordsByStudent.entrySet()) {
            String studentId = entry.getKey();
            List<AttendanceRecord> studentRecords = entry.getValue();
            
            long presentDays = studentRecords.stream()
                .filter(record -> "Present".equals(record.getStatus()))
                .count();
                
            Student student = studentMap.get(studentId);
            String studentName = (student != null) ? student.getName() : "Unknown";
            double percentage = (double) presentDays / workingDays * 100;
            
            System.out.printf("%-10s %-15s %-10d %-10.2f%%%n", 
                studentId, studentName, presentDays, percentage);
        }
    }
    
    // Export report to file
    public void exportReportToFile(String filename, LocalDate startDate, LocalDate endDate) {
        List<AttendanceRecord> exportRecords = attendanceRecords.stream()
            .filter(record -> !record.getDate().isBefore(startDate) && !record.getDate().isAfter(endDate))
            .collect(Collectors.toList());
            
        if (exportRecords.isEmpty()) {
            System.out.println("No records found for the specified period.");
            return;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/reports/" + filename))) {
            writer.println("Student ID,Date,Time,Status,Student Name");
            
            for (AttendanceRecord record : exportRecords) {
                Student student = studentMap.get(record.getStudentId());
                String studentName = (student != null) ? student.getName() : "Unknown";
                writer.printf("%s,%s,%s,%s,%s%n", 
                    record.getStudentId(), record.getDate(), record.getTime(), 
                    record.getStatus(), studentName);
            }
            
            System.out.println("Report exported successfully to: data/reports/" + filename);
        } catch (Exception e) {
            System.err.println("Error exporting report: " + e.getMessage());
        }
    }
    
    // Helper method to calculate working days (excluding weekends)
    private int calculateWorkingDays(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        int workingDays = 0;
        
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            java.time.DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek != java.time.DayOfWeek.SATURDAY && dayOfWeek != java.time.DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }
        return workingDays;
    }
    
    // Display all students
    public void displayAllStudents() {
        System.out.println("\n=== All Students ===");
        for (Student student : students) {
            System.out.printf("ID: %s, Name: %-15s, Department: %s%n", 
                student.getStudentId(), student.getName(), student.getDepartment());
        }
    }
    
    // Get students list for marking attendance
    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }
}
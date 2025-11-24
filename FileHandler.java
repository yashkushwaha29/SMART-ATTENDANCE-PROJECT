import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileHandler {
    private static final String STUDENT_FILE = "data/students.txt";
    private static final String ATTENDANCE_FILE = "data/attendance.txt";
    
    // Save students to file
    public static void saveStudents(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (Student student : students) {
                writer.println(student.toString());
            }
            System.out.println("Students saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }
    
    // Load students from file
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(STUDENT_FILE);
        
        // Create file if it doesn't exist
        if (!file.exists()) {
            createSampleStudents(students);
            return students;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    students.add(new Student(parts[0], parts[1], parts[2]));
                }
            }
            System.out.println("Students loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
        return students;
    }
    
    // Save attendance records
    public static void saveAttendance(List<AttendanceRecord> records) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTENDANCE_FILE, true))) {
            for (AttendanceRecord record : records) {
                writer.println(record.toString());
            }
            System.out.println("Attendance saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving attendance: " + e.getMessage());
        }
    }
    
    // Load all attendance records
    public static List<AttendanceRecord> loadAttendance() {
        List<AttendanceRecord> records = new ArrayList<>();
        File file = new File(ATTENDANCE_FILE);
        
        if (!file.exists()) {
            return records;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            String line;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    LocalDate date = LocalDate.parse(parts[1], dateFormatter);
                    LocalTime time = LocalTime.parse(parts[2], timeFormatter);
                    records.add(new AttendanceRecord(parts[0], date, time, parts[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading attendance: " + e.getMessage());
        }
        return records;
    }
    
    // Create sample students for first-time setup
    private static void createSampleStudents(List<Student> students) {
        students.add(new Student("S001", "Alice Johnson", "Computer Science"));
        students.add(new Student("S002", "Bob Smith", "Mathematics"));
        students.add(new Student("S003", "Carol Davis", "Physics"));
        students.add(new Student("S004", "David Wilson", "Computer Science"));
        students.add(new Student("S005", "Eva Brown", "Mathematics"));
        saveStudents(students);
    }
}
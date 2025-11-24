import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    private static AttendanceManager manager;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        manager = new AttendanceManager();
        scanner = new Scanner(System.in);
        
        System.out.println("=== Smart Attendance System ===");
        
        try {
            displayMainMenu();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            System.out.println("Thank you for using Smart Attendance System!");
        }
    }
    
    private static void displayMainMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View Daily Report");
            System.out.println("3. View Monthly Report");
            System.out.println("4. Export Report to File");
            System.out.println("5. Display All Students");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        markAttendance();
                        break;
                    case 2:
                        viewDailyReport();
                        break;
                    case 3:
                        viewMonthlyReport();
                        break;
                    case 4:
                        exportReport();
                        break;
                    case 5:
                        manager.displayAllStudents();
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private static void markAttendance() {
        System.out.println("\n=== Mark Attendance ===");
        manager.displayAllStudents();
        
        Set<String> presentStudents = new HashSet<>();
        System.out.println("\nEnter student IDs who are present (one per line, type 'done' to finish):");
        
        while (true) {
            System.out.print("Student ID: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            
            if (!input.isEmpty()) {
                presentStudents.add(input);
            }
        }
        
        manager.markAttendance(presentStudents);
    }
    
    private static void viewDailyReport() {
        System.out.println("\n=== View Daily Report ===");
        System.out.print("Enter date (YYYY-MM-DD) or press enter for today: ");
        String dateInput = scanner.nextLine().trim();
        
        LocalDate date;
        if (dateInput.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Using today's date.");
                date = LocalDate.now();
            }
        }
        
        manager.viewDailyReport(date);
    }
    
    private static void viewMonthlyReport() {
        System.out.println("\n=== View Monthly Report ===");
        
        try {
            System.out.print("Enter year (e.g., 2024): ");
            int year = scanner.nextInt();
            System.out.print("Enter month (1-12): ");
            int month = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (month < 1 || month > 12) {
                System.out.println("Invalid month! Using current month.");
                year = LocalDate.now().getYear();
                month = LocalDate.now().getMonthValue();
            }
            
            manager.viewMonthlyReport(year, month);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Using current month.");
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();
            manager.viewMonthlyReport(year, month);
            scanner.nextLine(); // Clear invalid input
        }
    }
    
    private static void exportReport() {
        System.out.println("\n=== Export Report ===");
        
        try {
            System.out.print("Enter filename (e.g., report.csv): ");
            String filename = scanner.nextLine();
            
            System.out.print("Enter start date (YYYY-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());
            
            System.out.print("Enter end date (YYYY-MM-DD): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());
            
            manager.exportReportToFile(filename, startDate, endDate);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format! Please use YYYY-MM-DD.");
        } catch (Exception e) {
            System.out.println("Error during export: " + e.getMessage());
        }
    }
}
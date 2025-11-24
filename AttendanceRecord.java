import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AttendanceRecord {
    private String studentId;
    private LocalDate date;
    private LocalTime time;
    private String status; // "Present", "Absent"
    
    public AttendanceRecord(String studentId, String status) {
        this.studentId = studentId;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.status = status;
    }
    
    public AttendanceRecord(String studentId, LocalDate date, LocalTime time, String status) {
        this.studentId = studentId;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getStatus() { return status; }
    
    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return studentId + "," + date.format(dateFormatter) + "," + 
               time.format(timeFormatter) + "," + status;
    }
}
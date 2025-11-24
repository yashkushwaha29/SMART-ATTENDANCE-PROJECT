import java.io.Serializable;

public class Student implements Serializable {
    private String studentId;
    private String name;
    private String department;
    
    public Student(String studentId, String name, String department) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    
    @Override
    public String toString() {
        return studentId + "," + name + "," + department;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId.equals(student.studentId);
    }
    
    @Override
    public int hashCode() {
        return studentId.hashCode();
    }
}
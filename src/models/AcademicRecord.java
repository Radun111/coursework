package models;

public class AcademicRecord {
    private int recordId;
    private int studentId;
    private int courseId;
    private String grade;
    private String status;
    private String term;
    private int year;

    public AcademicRecord(int recordId, int studentId, int courseId, String grade, String status, String term, int year) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
        this.status = status;
        this.term = term;
        this.year = year;
    }

    // Getters and Setters
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
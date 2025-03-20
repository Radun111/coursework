package models;

public class StudentAcademicRecord {
    private String courseID;
    private String courseName;
    private int credits;
    private String grade;
    private String status;
    private String term; 
    private int year;    

    public StudentAcademicRecord(String courseID, String courseName, int credits, String grade, String status, String term, int year) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.credits = credits;
        this.grade = grade;
        this.status = status;
        this.term = term; 
        this.year = year; 
    }

    // Getters and Setters
    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
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
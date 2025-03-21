package models;

public class PendingApproval {
    private int studentId;
    private String studentName;
    private int courseId;
    private String courseTitle;
    private String requestDate;

    public PendingApproval(int studentId, String studentName, int courseId, String courseTitle, String requestDate) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.requestDate = requestDate;
    }

    // Getters
    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getRequestDate() {
        return requestDate;
    }
}
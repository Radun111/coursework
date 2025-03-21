package models;

public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private String studentName;
    private int courseId;
    private String courseTitle;
    private String enrollmentDate;

    public Enrollment(int enrollmentId, int studentId, String studentName, int courseId, String courseTitle, String enrollmentDate) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.enrollmentDate = enrollmentDate;
    }

    // Getters
    public int getEnrollmentId() {
        return enrollmentId;
    }

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

    public String getEnrollmentDate() {
        return enrollmentDate;
    }
}
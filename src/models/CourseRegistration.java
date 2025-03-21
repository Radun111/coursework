package models;

public class CourseRegistration {
    private int studentId;
    private String fullName;
    private int courseId;
    private String courseName;
    private String status;
    private String registrationDate;

    public CourseRegistration(int studentId, String fullName, int courseId, String courseName, String status, String registrationDate) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.status = status;
        this.registrationDate = registrationDate;
    }

    // Getters
    public int getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getStatus() {
        return status;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }
}
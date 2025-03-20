package models;

public class Course {
    private int courseId;
    private String courseName;
    private int creditHours;
    private String instructor;
    private String department;
    private String prerequisites;
    private int maxCapacity;

    // Constructor, getters, and setters
    public Course(int courseId, String courseName, int creditHours, String instructor, String department, String prerequisites, int maxCapacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.instructor = instructor;
        this.department = department;
        this.prerequisites = prerequisites;
        this.maxCapacity = maxCapacity;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getDepartment() {
        return department;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}
package models;

import java.time.LocalDate;

public class Student {
    private int studentId;
    private String name;
    private LocalDate dateOfBirth;
    private String address;
    private String email;
    private String phoneNumber;

    // Constructor, getters, and setters
    public Student(int studentId, String name, LocalDate dateOfBirth, String address, String email, String phoneNumber) {
        this.studentId = studentId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
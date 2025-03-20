package models;

import java.time.LocalDate;

public class Student {
    private int studentId;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private LocalDate dateOfBirth;
    private String address;

    // Constructor with all fields
    public Student(int studentId, String name, String email, String phoneNumber, String password, LocalDate dateOfBirth, String address) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }
}
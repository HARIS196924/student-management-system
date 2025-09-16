package com.example.studentmanagment.dto;

import java.time.LocalDate;

public class StudentDTO {

    private Long id;
    private String name;
    private String email;
    private String course;
    private int age;
    private String address;
    private String phoneNumber;
    private String gender;
    private LocalDate enrollmentDate;
    private Long facultyId;

    // Default constructor
    public StudentDTO() {}

    public StudentDTO(Long id, String name, String email, String course, int age,
                      String address, String phoneNumber, String gender,
                      LocalDate enrollmentDate, Long facultyId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.enrollmentDate = enrollmentDate;
        this.facultyId = facultyId;
    }

    // Constructor
    public StudentDTO(Long id, String name, String email, String course, int age,
                      String address, String phoneNumber, String gender,
                      LocalDate enrollmentDate) {
        this(id, name, email, course, age, address, phoneNumber, gender, enrollmentDate, null);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
}

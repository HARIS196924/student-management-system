package com.example.studentmanagment.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "student")
public class StudentManagment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String course;

    @Column
    private int age;

    @Column
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private String gender;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "faculty_id")
    private Long facultyId;

    public StudentManagment() {
    }

    public StudentManagment(Long id, String name, String email, String course, int age,
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

    public StudentManagment(Long id, String name, String email, String course, int age,
                            String address, String phoneNumber, String gender,
                            LocalDate enrollmentDate) {
        this(id, name, email, course, age, address, phoneNumber, gender, enrollmentDate, null);
    }

    // Getters and setters
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

    // Override toString, equals, and hashCode for better logging and entity management

    @Override
    public String toString() {
        return "StudentManagment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                ", facultyId=" + facultyId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentManagment)) return false;
        StudentManagment that = (StudentManagment) o;
        return age == that.age &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(course, that.course) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(enrollmentDate, that.enrollmentDate) &&
                Objects.equals(facultyId, that.facultyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, course, age, address, phoneNumber, gender, enrollmentDate, facultyId);
    }
}

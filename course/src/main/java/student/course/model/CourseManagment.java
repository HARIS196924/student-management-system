package student.course.model;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class CourseManagment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseName;

    @Column(length = 1000)
    private String courseDescription;

    private int durationInMonths;

    @Column(unique = true, nullable = false)
    private String courseCode;

    private String instructorName;

    private Long facultyId;

    // Default constructor
    public CourseManagment() {}

    // Parameterized constructor
    public CourseManagment(Long id, String courseName, String courseDescription,
                           int durationInMonths, String courseCode, String instructorName, Long facultyId) {
        this.id = id;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.durationInMonths = durationInMonths;
        this.courseCode = courseCode;
        this.instructorName = instructorName;
        this.facultyId = facultyId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }
}

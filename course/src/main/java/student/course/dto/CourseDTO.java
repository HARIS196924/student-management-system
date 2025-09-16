package student.course.dto;

import student.course.model.CourseManagment;

import java.util.Objects;

public class CourseDTO {

    private Long id;
    private String courseName;
    private String courseDescription;
    private int durationInMonths;
    private String courseCode;
    private String instructorName;
    private Long facultyId;

    // Default constructor
    public CourseDTO() {}

    // All-args constructor
    public CourseDTO(Long id, String courseName, String courseDescription, int durationInMonths,
                     String courseCode, String instructorName, Long facultyId) {
        this.id = id;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.durationInMonths = durationInMonths;
        this.courseCode = courseCode;
        this.instructorName = instructorName;
        this.facultyId = facultyId;
    }

    // Constructor from entity
    public CourseDTO(CourseManagment course) {
        if (course != null) {
            this.id = course.getId();
            this.courseName = course.getCourseName();
            this.courseDescription = course.getCourseDescription();
            this.durationInMonths = course.getDurationInMonths();
            this.courseCode = course.getCourseCode();
            this.instructorName = course.getInstructorName();
            this.facultyId = course.getFacultyId();
        }
    }

    // Static factory method for clarity
    public static CourseDTO fromEntity(CourseManagment course) {
        return new CourseDTO(course);
    }

    // Convert DTO back to entity
    public CourseManagment toEntity() {
        CourseManagment course = new CourseManagment();
        course.setId(this.id);
        course.setCourseName(this.courseName);
        course.setCourseDescription(this.courseDescription);
        course.setDurationInMonths(this.durationInMonths);
        course.setCourseCode(this.courseCode);
        course.setInstructorName(this.instructorName);
        course.setFacultyId(this.facultyId);
        return course;
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

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseDTO)) return false;
        CourseDTO that = (CourseDTO) o;
        return durationInMonths == that.durationInMonths &&
                Objects.equals(id, that.id) &&
                Objects.equals(courseName, that.courseName) &&
                Objects.equals(courseDescription, that.courseDescription) &&
                Objects.equals(courseCode, that.courseCode) &&
                Objects.equals(instructorName, that.instructorName) &&
                Objects.equals(facultyId, that.facultyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseName, courseDescription, durationInMonths, courseCode, instructorName, facultyId);
    }

    // toString
    @Override
    public String toString() {
        return "CourseDTO{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", durationInMonths=" + durationInMonths +
                ", courseCode='" + courseCode + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", facultyId=" + facultyId +
                '}';
    }
}

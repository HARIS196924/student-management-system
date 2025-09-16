package student.course.service;

import student.course.dto.CourseDTO;

import java.util.List;

public interface CourseService {

    CourseDTO createCourse(CourseDTO courseDTO);

    List<CourseDTO> getAllCourses();

    CourseDTO getCourseById(Long id);

    CourseDTO updateCourse(Long id, CourseDTO courseDTO);

    void deleteCourse(Long id);

    boolean courseExistsByName(String name);

    boolean courseExistsByNameExcludingId(String name, Long id);

    CourseDTO getCourseByName(String courseName);
}

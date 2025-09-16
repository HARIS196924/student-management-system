package student.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import student.course.model.CourseManagment;

import java.util.Optional;
public interface CourseRepository extends JpaRepository<CourseManagment, Long> {

    boolean existsByCourseNameIgnoreCase(String courseName);

    boolean existsByCourseNameAndIdNot(String courseName, Long id);

    Optional<CourseManagment> findByCourseNameIgnoreCase(String courseName);
}

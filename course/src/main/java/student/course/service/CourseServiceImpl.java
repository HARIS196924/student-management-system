package student.course.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import student.course.dto.CourseDTO;
import student.course.model.CourseManagment;
import student.course.repository.CourseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        try {
            String name = courseDTO.getCourseName();

            if (courseRepository.existsByCourseNameIgnoreCase(name)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Course with the name '" + name + "' already exists.");
            }

            CourseManagment course = courseDTO.toEntity();
            CourseManagment saved = courseRepository.save(course);
            return new CourseDTO(saved);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating course", e);
        }
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        try {
            List<CourseManagment> courses = courseRepository.findAll();
            return courses.stream()
                    .map(CourseDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching all courses", e);
        }
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        try {
            CourseManagment course = courseRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with ID " + id));
            return new CourseDTO(course);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching course by ID", e);
        }
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        try {
            CourseManagment course = courseRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with ID " + id + " not found."));

            String name = courseDTO.getCourseName();

            if (courseRepository.existsByCourseNameAndIdNot(name, id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Another course with the name '" + name + "' already exists.");
            }

            course.setCourseName(name);
            course.setCourseDescription(courseDTO.getCourseDescription());
            course.setDurationInMonths(courseDTO.getDurationInMonths());
            course.setCourseCode(courseDTO.getCourseCode());
            course.setInstructorName(courseDTO.getInstructorName());
            course.setFacultyId(courseDTO.getFacultyId());

            CourseManagment updated = courseRepository.save(course);
            return new CourseDTO(updated);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating course", e);
        }
    }

    @Override
    public void deleteCourse(Long id) {
        try {
            if (!courseRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with ID " + id + " does not exist.");
            }
            courseRepository.deleteById(id);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting course", e);
        }
    }

    @Override
    public boolean courseExistsByName(String courseName) {
        try {
            return courseRepository.existsByCourseNameIgnoreCase(courseName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error checking course existence", e);
        }
    }

    @Override
    public boolean courseExistsByNameExcludingId(String courseName, Long id) {
        try {
            return courseRepository.existsByCourseNameAndIdNot(courseName, id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error checking course name excluding ID", e);
        }
    }

    @Override
    public CourseDTO getCourseByName(String courseName) {
        try {
            CourseManagment course = courseRepository.findByCourseNameIgnoreCase(courseName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with name: " + courseName));
            return new CourseDTO(course);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching course by name", e);
        }
    }
}

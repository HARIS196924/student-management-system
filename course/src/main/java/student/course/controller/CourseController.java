package student.course.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import student.course.dto.CourseDTO;
import student.course.service.CourseService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final RestTemplate restTemplate;

    @Value("${student.service.url}")
    private String studentServiceUrl;

    @Value("${faculty.service.url}")
    private String facultyServiceUrl;

    public CourseController(CourseService courseService, RestTemplate restTemplate) {
        this.courseService = courseService;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseDTO courseDTO) {
        try {
            if (courseService.courseExistsByName(courseDTO.getCourseName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Course with name '" + courseDTO.getCourseName() + "' already exists.");
            }

            CourseDTO createdCourse = courseService.createCourse(courseDTO);
            return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating course: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<CourseDTO> courseDTOs = courseService.getAllCourses();
            return ResponseEntity.ok(courseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching courses: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            CourseDTO courseDTO = courseService.getCourseById(id);
            if (courseDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Course not found with ID: " + id);
            }
            return ResponseEntity.ok(courseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching course: " + e.getMessage());
        }
    }

    @GetMapping("/exists/{courseName}")
    public ResponseEntity<?> checkIfCourseExists(@PathVariable String courseName) {
        try {
            boolean exists = courseService.courseExistsByName(courseName);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking course existence: " + e.getMessage());
        }
    }

    @GetMapping("/details/{courseName}")
    public ResponseEntity<?> getCourseDetailsByName(@PathVariable String courseName) {
        try {
            CourseDTO course = courseService.getCourseByName(courseName);
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Course not found with name: " + courseName);
            }
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching course details: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        try {
            if (courseService.courseExistsByNameExcludingId(courseDTO.getCourseName(), id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Another course with name '" + courseDTO.getCourseName() + "' already exists.");
            }

            CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
            return ResponseEntity.ok(updatedCourse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating course: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course with ID " + id + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting course: " + e.getMessage());
        }
    }

    // INTER-SERVICE COMMUNICATION

    @GetMapping("/{courseName}/faculty")
    public ResponseEntity<?> getFacultyByCourseName(@PathVariable String courseName) {
        try {
            CourseDTO course = courseService.getCourseByName(courseName);
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Course not found: " + courseName);
            }

            Long facultyId = course.getFacultyId();
            if (facultyId == null) {
                return ResponseEntity.ok("No faculty assigned to course: " + courseName);
            }

            String url = facultyServiceUrl + "/" + facultyId;
            ResponseEntity<Map> facultyResponse = restTemplate.getForEntity(url, Map.class);

            return ResponseEntity.ok(facultyResponse.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching faculty for course: " + e.getMessage());
        }
    }

    @GetMapping("/{courseName}/students")
    public ResponseEntity<?> getStudentsByCourse(@PathVariable String courseName) {
        try {
            String url = studentServiceUrl + "/search/course?course=" + courseName;

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> students = response.getBody();
            if (students == null || students.isEmpty()) {
                return ResponseEntity.ok("No students found enrolled in course: " + courseName);
            }
            return ResponseEntity.ok(students);
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch students from Student Service: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    public String getFacultyServiceUrl() {
        return facultyServiceUrl;
    }

    public void setFacultyServiceUrl(String facultyServiceUrl) {
        this.facultyServiceUrl = facultyServiceUrl;
    }
}

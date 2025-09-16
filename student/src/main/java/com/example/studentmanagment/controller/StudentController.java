package com.example.studentmanagment.controller;

import com.example.studentmanagment.dto.StudentDTO;
import com.example.studentmanagment.service.StudentCRUDService;
import com.example.studentmanagment.service.StudentSearchService;
import com.example.studentmanagment.service.StudentStatisticsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentCRUDService crudService;
    private final StudentSearchService searchService;
    private final StudentStatisticsService statisticsService;
    private final RestTemplate restTemplate;

    @Value("${course.service.url}")
    private String courseServiceUrl;

    @Value("${faculty.service.url}")
    private String facultyServiceUrl;

    public StudentController(StudentCRUDService crudService,
                             StudentSearchService searchService,
                             StudentStatisticsService statisticsService,
                             RestTemplate restTemplate) {
        this.crudService = crudService;
        this.searchService = searchService;
        this.statisticsService = statisticsService;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<?> saveStudent(@RequestBody StudentDTO studentDTO) {
        try {
            validateCourseExistence(studentDTO.getCourse());

            if (crudService.existsByEmail(studentDTO.getEmail())) {
                return ResponseEntity.badRequest().body("Student with email '" + studentDTO.getEmail() + "' already exists.");
            }
            if (crudService.existsByName(studentDTO.getName())) {
                return ResponseEntity.badRequest().body("Student with name '" + studentDTO.getName() + "' already exists.");
            }


            StudentDTO saved = crudService.saveStudent(studentDTO);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving student: " + e.getMessage());
        }
    }

    @GetMapping("/search/by-faculty")
    public ResponseEntity<?> getStudentsByFaculty(@RequestParam Long facultyId) {
        try {
            List<StudentDTO> students = searchService.searchByFacultyId(facultyId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching students by faculty ID: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        try {
            StudentDTO student = crudService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Student not found with ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<StudentDTO> students = crudService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching students: " + e.getMessage());
        }
    }

    @GetMapping("/search/course")
    public ResponseEntity<?> searchByCourse(@RequestParam String course) {
        try {
            List<StudentDTO> students = searchService.searchByCourse(course);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error searching students by course: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        try {
            validateCourseExistence(studentDTO.getCourse());

            if (crudService.existsByEmailExcludingId(studentDTO.getEmail(), id)) {
                return ResponseEntity.badRequest().body("Email '" + studentDTO.getEmail() + "' is already used by another student.");
            }

            StudentDTO updated = crudService.updateStudent(id, studentDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating student: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/change-course")
    public ResponseEntity<?> changeCourse(@PathVariable Long id, @RequestParam String newCourse) {
        try {
            validateCourseExistence(newCourse);

            StudentDTO student = crudService.getStudentById(id);
            student.setCourse(newCourse);
            StudentDTO updated = crudService.updateStudent(id, student);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error changing course: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            crudService.deleteStudent(id);
            return ResponseEntity.ok("Student with ID " + id + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting student: " + e.getMessage());
        }
    }

    //INTER-SERVICE COMMUNICATION
    private void validateCourseExistence(String courseName) {
        try {
            String url = courseServiceUrl + "/exists/" + courseName;
            Boolean exists = restTemplate.getForObject(url, Boolean.class);
            if (exists == null || !exists) {
                throw new RuntimeException("Course '" + courseName + "' does not exist.");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to validate course existence.", e);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<?> getStudentDetailsByName(@RequestParam String name) {
        try {
            StudentDTO student = crudService.getStudentByName(name);
            if (student == null) {
                return ResponseEntity.status(404).body("Student with name '" + name + "' not found.");
            }

            String courseUrl = courseServiceUrl + "/details/" + student.getCourse();
            ResponseEntity<?> courseResponse = restTemplate.getForEntity(courseUrl, Object.class);

            String facultyUrl = facultyServiceUrl + "/" + student.getFacultyId();
            ResponseEntity<?> facultyResponse = restTemplate.getForEntity(facultyUrl, Object.class);

            return ResponseEntity.ok(Map.of("student", student, "course", courseResponse.getBody(),
                    "faculty", facultyResponse.getBody()
            ));

        } catch (RestClientException e) {
            return ResponseEntity.status(500).body("Error fetching details: " + e.getMessage());
        }
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getAllCoursesFromCourseService() {
        try {
            List<?> courses = restTemplate.getForObject(courseServiceUrl, List.class);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching courses from course service: " + e.getMessage());
        }
    }

    public String getFacultyServiceUrl() {
        return facultyServiceUrl;
    }

    public void setFacultyServiceUrl(String facultyServiceUrl) {
        this.facultyServiceUrl = facultyServiceUrl;
    }
}

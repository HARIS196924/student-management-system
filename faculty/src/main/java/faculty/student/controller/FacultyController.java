package faculty.student.controller;

import faculty.student.dto.FacultyDTO;
import faculty.student.service.FacultyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@RestController
@RequestMapping("/api/faculties")
public class FacultyController {

    private final FacultyService facultyService;
    private final RestTemplate restTemplate;

    @Value("${student.service.url}")
    private String studentServiceUrl;

    @Value("${course.service.url}")
    private String courseServiceUrl;

    public FacultyController(FacultyService facultyService, RestTemplate restTemplate) {
        this.facultyService = facultyService;
        this.restTemplate = restTemplate;
    }

    //CREATE
    @PostMapping
    public ResponseEntity<?> createFaculty(@RequestBody FacultyDTO facultyDTO) {
        try {
            if (facultyService.existsByEmail(facultyDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Faculty with email '" + facultyDTO.getEmail() + "' already exists.");
            }

            FacultyDTO createdFaculty = facultyService.createFaculty(facultyDTO);
            return new ResponseEntity<>(createdFaculty, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating faculty: " + e.getMessage());
        }
    }

    //READ
    @GetMapping("/{id}")
    public ResponseEntity<?> getFacultyById(@PathVariable Long id) {
        try {
            FacultyDTO facultyDTO = facultyService.getFacultyById(id);
            return ResponseEntity.ok(facultyDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Faculty not found with ID: " + id);
        }
    }

    //INTER-SERVICE COMMUNICATION
    @GetMapping("/{facultyId}/students")
    public ResponseEntity<?> getStudentsByFacultyId(@PathVariable Long facultyId) {
        try {
            String url = studentServiceUrl + "/search/by-faculty?facultyId=" + facultyId;
            List<?> students = restTemplate.getForObject(url, List.class);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch students for faculty ID " + facultyId + ": " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFaculties() {
        try {
            List<FacultyDTO> faculties = facultyService.getAllFaculties();
            return ResponseEntity.ok(faculties);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving faculties: " + e.getMessage());
        }
    }

    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFaculty(@PathVariable Long id, @RequestBody FacultyDTO facultyDTO) {
        try {
            if (facultyService.existsByEmailExcludingId(facultyDTO.getEmail(), id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Another faculty with email '" + facultyDTO.getEmail() + "' already exists.");
            }

            FacultyDTO updatedFaculty = facultyService.updateFaculty(id, facultyDTO);
            return ResponseEntity.ok(updatedFaculty);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating faculty: " + e.getMessage());
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFaculty(@PathVariable Long id) {
        try {
            facultyService.deleteFaculty(id);
            return ResponseEntity.ok("Faculty with ID " + id + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting faculty: " + e.getMessage());
        }
    }

    //INTER-SERVICE COMMUNICATION
    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        try {
            String url = studentServiceUrl;
            ResponseEntity<List<?>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<?>>() {}
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch students from Student Service: " + e.getMessage());
        }
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
        try {
            String url = courseServiceUrl;
            ResponseEntity<List<?>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<?>>() {}
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch courses from Course Service: " + e.getMessage());
        }
    }
}

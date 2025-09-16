package com.example.studentmanagment.service;

import com.example.studentmanagment.dto.StudentDTO;
import com.example.studentmanagment.model.StudentManagment;
import com.example.studentmanagment.repository.StudentManagmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentManagmentServiceImpl implements StudentCRUDService, StudentSearchService, StudentStatisticsService {

    private final StudentManagmentRepository repository;
    private final RestTemplate restTemplate;

    @Value("${course.service.url}")
    private String courseServiceUrl;

    @Value("${faculty.service.url}")
    private String facultyServiceUrl;

    public StudentManagmentServiceImpl(StudentManagmentRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    private StudentDTO convertToDTO(StudentManagment entity) {
        return new StudentDTO(
                entity.getId(), entity.getName(), entity.getEmail(), entity.getCourse(), entity.getAge(),
                entity.getAddress(), entity.getPhoneNumber(), entity.getGender(),
                entity.getEnrollmentDate(), entity.getFacultyId()
        );
    }

    private StudentManagment convertToEntity(StudentDTO dto) {
        return new StudentManagment(
                dto.getId(), dto.getName(), dto.getEmail(), dto.getCourse(), dto.getAge(),
                dto.getAddress(), dto.getPhoneNumber(), dto.getGender(),
                dto.getEnrollmentDate(), dto.getFacultyId()
        );
    }

    @Override
    public StudentDTO saveStudent(StudentDTO studentDTO) {
        try {
            String courseName = studentDTO.getCourse().trim();
            Boolean courseExists = restTemplate.getForObject(courseServiceUrl + "/exists/" + courseName, Boolean.class);
            if (Boolean.FALSE.equals(courseExists)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course '" + courseName + "' does not exist.");
            }

            String email = studentDTO.getEmail().trim().toLowerCase();
            String name = studentDTO.getName().trim();

            if (repository.existsByEmail(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with email '" + email + "' already exists.");
            }

            if (repository.existsByName(name)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with name '" + name + "' already exists.");
            }

            studentDTO.setEmail(email);
            studentDTO.setName(name);

            return convertToDTO(repository.save(convertToEntity(studentDTO)));
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Course validation service is unavailable", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save student", e);
        }
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        try {
            return repository.findById(id)
                    .map(this::convertToDTO)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with ID " + id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch student", e);
        }
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        try {
            return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch students", e);
        }
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        try {
            StudentManagment student = repository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with ID " + id));

            String newEmail = studentDTO.getEmail().trim().toLowerCase();
            String newName = studentDTO.getName().trim();
            String newCourse = studentDTO.getCourse().trim();

            Boolean courseExists = restTemplate.getForObject(courseServiceUrl + "/exists/" + newCourse, Boolean.class);
            if (Boolean.FALSE.equals(courseExists)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course '" + newCourse + "' does not exist.");
            }

            if (!student.getEmail().equalsIgnoreCase(newEmail) && repository.existsByEmail(newEmail)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email '" + newEmail + "' is already used by another student.");
            }

            if (!student.getName().equalsIgnoreCase(newName) && repository.existsByName(newName)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with name '" + newName + "' already exists.");
            }

            student.setName(newName);
            student.setEmail(newEmail);
            student.setCourse(newCourse);
            student.setAge(studentDTO.getAge());
            student.setAddress(studentDTO.getAddress());
            student.setPhoneNumber(studentDTO.getPhoneNumber());
            student.setGender(studentDTO.getGender());
            student.setEnrollmentDate(studentDTO.getEnrollmentDate());

            return convertToDTO(repository.save(student));
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Course validation service is unavailable", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update student", e);
        }
    }

    @Override
    public void deleteStudent(Long id) {
        try {
            if (!repository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with ID " + id + " does not exist.");
            }
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete student", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return repository.existsByEmail(email.trim().toLowerCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to check email existence", e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        try {
            return repository.existsByName(name.trim());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to check name existence", e);
        }
    }

    @Override
    public boolean existsByEmailExcludingId(String email, Long id) {
        try {
            StudentManagment student = repository.findByEmail(email.trim().toLowerCase());
            return student != null && !student.getId().equals(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to check email exclusion", e);
        }
    }

    @Override
    public StudentDTO getStudentByName(String name) {
        try {
            StudentManagment student = repository.findByName(name.trim());
            if (student == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with name '" + name + "'");
            }
            return convertToDTO(student);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch student by name", e);
        }
    }

    @Override
    public List<StudentDTO> searchByName(String name) {
        try {
            return repository.findByNameContaining(name.trim())
                    .stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search by name", e);
        }
    }

    @Override
    public List<StudentDTO> searchByCourse(String course) {
        try {
            return repository.findByCourse(course.trim())
                    .stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search by course", e);
        }
    }

    @Override
    public List<StudentDTO> searchByAgeRange(int from, int to) {
        try {
            return repository.findByAgeBetween(from, to)
                    .stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search by age range", e);
        }
    }

    @Override
    public List<StudentDTO> searchByFacultyId(Long facultyId) {
        try {
            return repository.findByFacultyId(facultyId)
                    .stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search by faculty ID", e);
        }
    }

    @Override
    public long getTotalStudentCount() {
        try {
            return repository.count();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to count total students", e);
        }
    }

    @Override
    public long getStudentCountByCourse(String course) {
        try {
            return repository.countByCourse(course.trim());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to count students by course", e);
        }
    }

    public String getFacultyServiceUrl() {
        return facultyServiceUrl;
    }

    public void setFacultyServiceUrl(String facultyServiceUrl) {
        this.facultyServiceUrl = facultyServiceUrl;
    }
}

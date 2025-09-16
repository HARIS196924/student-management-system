package com.example.studentmanagment.service;

import com.example.studentmanagment.dto.StudentDTO;
import java.util.List;

public interface StudentCRUDService {

    // Create
    StudentDTO saveStudent(StudentDTO studentDTO);

    // Read
    StudentDTO getStudentById(Long id);
    List<StudentDTO> getAllStudents();

    // Update
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);

    // Delete
    void deleteStudent(Long id);

    // Validation checks
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    boolean existsByEmailExcludingId(String email, Long id);

    StudentDTO getStudentByName(String name);
}

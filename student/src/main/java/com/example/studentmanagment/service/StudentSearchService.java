package com.example.studentmanagment.service;

import com.example.studentmanagment.dto.StudentDTO;
import java.util.List;

public interface StudentSearchService {
    List<StudentDTO> searchByName(String name);
    List<StudentDTO> searchByCourse(String course);
    List<StudentDTO> searchByAgeRange(int from, int to);
    List<StudentDTO> searchByFacultyId(Long facultyId);
}

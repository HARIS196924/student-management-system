package com.example.studentmanagment.repository;

import com.example.studentmanagment.model.StudentManagment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentManagmentRepository extends JpaRepository<StudentManagment, Long> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    boolean existsByEmailAndIdNot(String email, Long id);
    List<StudentManagment> findByNameContaining(String name);
    List<StudentManagment> findByCourse(String course);
    List<StudentManagment> findByAgeBetween(int from, int to);
    long countByCourse(String course);

    StudentManagment findByEmail(String email);

    StudentManagment findByName(String name);

    List<StudentManagment> findByFacultyId(Long facultyId);
}

package faculty.student.service;

import faculty.student.dto.FacultyDTO;
import java.util.List;

public interface FacultyService {
    FacultyDTO createFaculty(FacultyDTO facultyDTO);
    FacultyDTO getFacultyById(Long id);
    List<FacultyDTO> getAllFaculties();
    FacultyDTO updateFaculty(Long id, FacultyDTO facultyDTO);
    void deleteFaculty(Long id);
    boolean existsByEmail(String email);

    boolean existsByEmailExcludingId(String email, Long id);

}

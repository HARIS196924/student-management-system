package faculty.student.service;

import faculty.student.dto.FacultyDTO;
import faculty.student.model.FacultyEntity;
import faculty.student.repository.FacultyRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyImpl implements FacultyService {

    @Autowired
    private FacultyRespository facultyRepository;

    private FacultyDTO mapToDTO(FacultyEntity facultyEntity) {
        return new FacultyDTO(
                facultyEntity.getId(),
                facultyEntity.getName(),
                facultyEntity.getDepartment(),
                facultyEntity.getEmail()
        );
    }

    private FacultyEntity mapToEntity(FacultyDTO facultyDTO) {
        return new FacultyEntity(
                facultyDTO.getId(),
                facultyDTO.getName(),
                facultyDTO.getDepartment(),
                facultyDTO.getEmail()
        );
    }

    @Override
    public FacultyDTO createFaculty(FacultyDTO facultyDTO) {
        try {
            FacultyEntity facultyEntity = mapToEntity(facultyDTO);
            FacultyEntity savedEntity = facultyRepository.save(facultyEntity);
            return mapToDTO(savedEntity);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating faculty", e);
        }
    }

    @Override
    public FacultyDTO getFacultyById(Long id) {
        try {
            FacultyEntity facultyEntity = facultyRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found with id: " + id));
            return mapToDTO(facultyEntity);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching faculty by ID", e);
        }
    }

    @Override
    public List<FacultyDTO> getAllFaculties() {
        try {
            List<FacultyEntity> faculties = facultyRepository.findAll();
            return faculties.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching all faculties", e);
        }
    }

    @Override
    public FacultyDTO updateFaculty(Long id, FacultyDTO facultyDTO) {
        try {
            FacultyEntity existingFaculty = facultyRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found with id: " + id));

            existingFaculty.setName(facultyDTO.getName());
            existingFaculty.setDepartment(facultyDTO.getDepartment());
            existingFaculty.setEmail(facultyDTO.getEmail());

            FacultyEntity updatedEntity = facultyRepository.save(existingFaculty);
            return mapToDTO(updatedEntity);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating faculty", e);
        }
    }

    @Override
    public void deleteFaculty(Long id) {
        try {
            FacultyEntity facultyEntity = facultyRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found with id: " + id));
            facultyRepository.delete(facultyEntity);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting faculty", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return facultyRepository.existsByEmailIgnoreCase(email.trim().toLowerCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error checking email existence", e);
        }
    }

    @Override
    public boolean existsByEmailExcludingId(String email, Long id) {
        return false;
    }
}

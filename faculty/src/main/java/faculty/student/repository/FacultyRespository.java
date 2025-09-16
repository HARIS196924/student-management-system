package faculty.student.repository;

import faculty.student.model.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRespository extends JpaRepository<FacultyEntity, Long> {

        boolean existsByEmail(String email);
        boolean existsByEmailIgnoreCase(String email);

}

package mn.icode.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import mn.icode.model.Doctor;
import mn.icode.repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    public Page<Doctor> searchDoctorsPaginated(String name, Long departmentId, String specialization, Pageable pageable) {
    	Specification<Doctor> spec = (root, query, criteriaBuilder) -> {
    		List<Predicate> predicates = new ArrayList<>();

    		if (name != null && !name.trim().isEmpty()) {
    			String searchPattern = "%" + name.trim().toLowerCase() + "%";
    			Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), searchPattern);
    			Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchPattern);
    			predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch));
    		}

    		if (departmentId != null) {
    			predicates.add(criteriaBuilder.equal(root.get("department").get("id"), departmentId));
    		}

    		if (specialization != null && !specialization.trim().isEmpty()) {
    			String specPattern = "%" + specialization.trim().toLowerCase() + "%";
    			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("specialization")), specPattern));
    		}

    		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    	};

    	return doctorRepository.findAll(spec, pageable);
    }
}
package mn.icode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mn.icode.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
package mn.icode.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mn.icode.model.Role;
import mn.icode.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	long countByRole(Role role);
}

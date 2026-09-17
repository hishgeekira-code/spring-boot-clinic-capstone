package mn.icode.config;

import mn.icode.model.Role;
import mn.icode.model.User;
import mn.icode.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // ADMIN хэрэглэгч байхгүй бол шинээр үүсгэнэ
        if (userRepository.findByEmail("admin@clinic.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@clinic.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("99111111");
            admin.setRole(Role.ADMIN); // Role enum ашиглав
            userRepository.save(admin);
        }

        // CUSTOMER хэрэглэгч байхгүй бол шинээр үүсгэнэ
        if (userRepository.findByEmail("customer@clinic.com").isEmpty()) {
            User customer = new User();
            customer.setFirstName("John");
            customer.setLastName("Doe");
            customer.setEmail("customer@clinic.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setPhone("88111111");
            customer.setRole(Role.CUSTOMER); // Role enum ашиглав
            userRepository.save(customer);
        }
    }
}
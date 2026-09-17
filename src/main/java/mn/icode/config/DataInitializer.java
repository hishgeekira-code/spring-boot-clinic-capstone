package mn.icode.config;

import mn.icode.model.*;
import mn.icode.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, ScheduleRepository scheduleRepository, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
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
        
        if (scheduleRepository.count() == 0 && doctorRepository.count() > 0) {
            Doctor doctor = doctorRepository.findAll().get(0);
            Schedule schedule = new Schedule();
            schedule.setDoctor(doctor);
            schedule.setAvailableDate(LocalDate.now().plusDays(1));
            schedule.setStartTime(LocalTime.of(9, 0));
            schedule.setEndTime(LocalTime.of(10, 0));
            schedule.setAvailable(true);
            scheduleRepository.save(schedule);
        }
    }
}
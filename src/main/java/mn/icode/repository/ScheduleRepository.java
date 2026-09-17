package mn.icode.repository;

import mn.icode.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    List<Schedule> findByDoctorId(Long doctorId);
    
    List<Schedule> findByDoctorIdAndAvailableTrue(Long doctorId);
    
    List<Schedule> findByAvailableDate(LocalDate date);
}
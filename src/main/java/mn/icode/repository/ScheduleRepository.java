package mn.icode.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mn.icode.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDoctorId(Long doctorId);

    List<Schedule> findByDoctorIdAndAvailableTrue(Long doctorId);

    List<Schedule> findByAvailableDate(LocalDate date);
}
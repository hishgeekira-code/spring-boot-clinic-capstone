package mn.icode.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mn.icode.model.Appointment;
import mn.icode.model.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByPatientId(Long patientId);

	List<Appointment> findByDoctorId(Long doctorId);

	boolean existsByScheduleId(Long scheduleId);
	boolean existsByScheduleIdAndStatusNot(Long scheduleId, AppointmentStatus status);

	long countByStatus(AppointmentStatus status);

	@Query("SELECT COUNT(a) FROM Appointment a WHERE a.schedule.availableDate = :today")
	long countTodayAppointments(@Param("today") LocalDate today);
}

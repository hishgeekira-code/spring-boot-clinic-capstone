package mn.icode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mn.icode.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	
	List<Appointment> findByPatientId(Long patientId);
	
	List<Appointment> findByDoctorId(Long doctorId);
	
	boolean existsByScheduleId(Long scheduleId);
}

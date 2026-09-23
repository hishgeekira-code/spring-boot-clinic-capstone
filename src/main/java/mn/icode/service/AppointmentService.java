package mn.icode.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mn.icode.model.Appointment;
import mn.icode.model.AppointmentStatus;
import mn.icode.model.Doctor;
import mn.icode.model.Schedule;
import mn.icode.model.User;
import mn.icode.repository.AppointmentRepository;
import mn.icode.repository.ScheduleRepository;
import mn.icode.repository.UserRepository;

@Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final ScheduleRepository scheduleRepository;
	private final UserRepository userRepository;

	public AppointmentService(AppointmentRepository appointmentRepository,
			                  ScheduleRepository scheduleRepository,
							  UserRepository userRepository) {
		this.appointmentRepository = appointmentRepository;
		this.scheduleRepository = scheduleRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public Appointment bookAppointment(Long patientId, Long scheduleId, String reason) {
		User patient = userRepository.findById(patientId)
				.orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

		Schedule schedule = scheduleRepository.findById(scheduleId)
				.orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleId));

		if (!schedule.isAvailable()) {
			throw new IllegalStateException("This schedule time slot is already booked or unavailable.");
		}

		if (appointmentRepository.existsByScheduleIdAndStatusNot(scheduleId, AppointmentStatus.CANCELLED)) {
		    throw new IllegalStateException("An active appointment already exists for this schedule.");
		}

		Doctor doctor = schedule.getDoctor();

		// Tsag zahialga vvsgeh
		Appointment appointment = new Appointment();
		appointment.setPatient(patient);
		appointment.setDoctor(doctor);
		appointment.setSchedule(schedule);
		appointment.setReason(reason);
		appointment.setStatus(AppointmentStatus.PENDING);

		// Tsagiin huwaariig bolomjgvi (booked) tuluwt oruulna
		schedule.setAvailable(false);
		scheduleRepository.save(schedule);

		return appointmentRepository.save(appointment);
	}

	@Transactional
	public Appointment updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
	    Appointment appointment = appointmentRepository.findById(appointmentId)
	            .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));

	    appointment.setStatus(newStatus);

	    // Хэрэв цуцлагдвал хуваарийг суллах
	    if (appointment.getSchedule() != null) {
	        if (newStatus == AppointmentStatus.CANCELLED) {
	            appointment.getSchedule().setAvailable(true);
	        } else {
	            appointment.getSchedule().setAvailable(false);
	        }
	    }

	    // Заавал saveAndFlush хийж шууд бааз руу UPDATE бичүүлнэ
	    return appointmentRepository.saveAndFlush(appointment);
	}

	@Transactional
	public void cancelAppointment(Long appointmentId, Long currentUserId) {
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

		if (!appointment.getPatient().getId().equals(currentUserId)) {
			throw new IllegalStateException("You are not authorized to cancel this appointment.");
		}

		if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
			throw new IllegalStateException("Cannot cancel an already completed appointment.");
		}

		if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
			throw new IllegalStateException("Appontment is already cancelled.");
		}

		appointment.setStatus(AppointmentStatus.CANCELLED);

		// Zahialga tsutslagdahad huwaariig butsaaj bolomjtoi bolgono
		Schedule schedule = appointment.getSchedule();
		if (schedule != null) {
			schedule.setAvailable(true);
			scheduleRepository.save(schedule);
		}

		appointmentRepository.save(appointment);
	}

	public List<Appointment> getAppointmentsByPatient(Long patientId) {
		return appointmentRepository.findByPatientId(patientId);
	}

	public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
		return appointmentRepository.findByDoctorId(doctorId);
	}

	public List<Appointment> getAllAppointments() {
		return appointmentRepository.findAll();
	}

	public Appointment getAppointmentById(Long id) {
		return appointmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
	}
	
	public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
	    return appointmentRepository.findByStatus(status);
	}
}

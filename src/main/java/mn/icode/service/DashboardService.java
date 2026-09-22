package mn.icode.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import mn.icode.dto.DashboardStatsDto;
import mn.icode.model.AppointmentStatus;
import mn.icode.model.Role;
import mn.icode.repository.AppointmentRepository;
import mn.icode.repository.DoctorRepository;
import mn.icode.repository.UserRepository;

@Service
public class DashboardService {

	private final DoctorRepository doctorRepository;
	private final UserRepository userRepository;
	private final AppointmentRepository appointmentRepository;

	public DashboardService(DoctorRepository doctorRepository, UserRepository userRepository,
			AppointmentRepository appointmentRepository) {
		this.doctorRepository = doctorRepository;
		this.userRepository = userRepository;
		this.appointmentRepository = appointmentRepository;
	}

	public DashboardStatsDto getDashboardStatistics() {
		long totalDoctors = doctorRepository.count();
		long totalPatients = userRepository.countByRole(Role.CUSTOMER);
		long todayAppointments = appointmentRepository.countTodayAppointments(LocalDate.now());
		long bookedAppointments = appointmentRepository.countByStatus(AppointmentStatus.CONFIRMED);
		long completedAppointments = appointmentRepository.countByStatus(AppointmentStatus.COMPLETED);
		long cancelledAppointments = appointmentRepository.countByStatus(AppointmentStatus.CANCELLED);

		return new DashboardStatsDto(totalDoctors,
									 totalPatients,
									 todayAppointments,
									 bookedAppointments,
									 completedAppointments,
									 cancelledAppointments
		);
	}
}

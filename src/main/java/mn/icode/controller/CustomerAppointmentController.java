package mn.icode.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mn.icode.model.Schedule;
import mn.icode.model.User;
import mn.icode.repository.UserRepository;
import mn.icode.service.AppointmentService;
import mn.icode.service.ScheduleService;

@Controller
public class CustomerAppointmentController {

	private final AppointmentService appointmentService;
	private final ScheduleService scheduleService;
	private final UserRepository userRepository;

	public CustomerAppointmentController(AppointmentService appointmentService, 
	                                     ScheduleService scheduleService,
	                                     UserRepository userRepository) {
		this.appointmentService = appointmentService;
		this.scheduleService = scheduleService;
		this.userRepository = userRepository;
	}

	@GetMapping("/my-appointments")
	public String myAppointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		User user = userRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		model.addAttribute("appointments", appointmentService.getAppointmentsByPatient(user.getId()));
		return "customer/my-appointments";
	}

	// templates/customer/book-appointment.html хуудсыг дуудах хэсэг
	@GetMapping({ "/appointments/book", "/appointments/book/{scheduleId}" })
	public String showBookingForm(@PathVariable(required = false) Long scheduleId,
	                              @RequestParam(value = "scheduleId", required = false) Long paramScheduleId, 
	                              Model model,
	                              RedirectAttributes redirectAttributes) {
		Long targetId = (scheduleId != null) ? scheduleId : paramScheduleId;
		if (targetId == null) {
			return "redirect:/doctors";
		}

		try {
			Schedule schedule = scheduleService.getScheduleById(targetId);

			if (!schedule.isAvailable()) {
				redirectAttributes.addFlashAttribute("errorMessage", "This schedule is no longer available.");
				return "redirect:/doctors";
			}

			model.addAttribute("schedule", schedule);
			return "customer/book-appointment"; 
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Schedule not found: " + e.getMessage());
			return "redirect:/doctors";
		}
	}

	@PostMapping("/appointments/book")
	public String bookAppointment(@AuthenticationPrincipal UserDetails userDetails, 
	                              @RequestParam Long scheduleId,
	                              @RequestParam(required = false) String reason, 
	                              RedirectAttributes redirectAttributes) {
		User user = userRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		try {
			appointmentService.bookAppointment(user.getId(), scheduleId, reason);
			redirectAttributes.addFlashAttribute("successMessage", "Appointment booked successfully!");
		} catch (IllegalStateException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/doctors";
		}

		return "redirect:/my-appointments";
	}

	@PostMapping("/appointments/cancel/{id}")
	public String cancelAppointment(@PathVariable Long id, 
	                               @AuthenticationPrincipal UserDetails userDetails,
	                               RedirectAttributes redirectAttributes) {
		User user = userRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		try {
			appointmentService.cancelAppointment(id, user.getId());
			redirectAttributes.addFlashAttribute("successMessage", "Appointment cancelled successfully.");
		} catch (IllegalStateException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/my-appointments";
	}
}
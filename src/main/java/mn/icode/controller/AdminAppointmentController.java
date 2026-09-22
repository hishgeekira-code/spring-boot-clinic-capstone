package mn.icode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mn.icode.model.AppointmentStatus;
import mn.icode.service.AppointmentService;

@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

	private final AppointmentService appointmentService;

	public AdminAppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@GetMapping
	public String listAppointments(Model model) {
		model.addAttribute("appointments", appointmentService.getAllAppointments());
		model.addAttribute("statuses", AppointmentStatus.values());
		return "admin/appointments/index";
	}

	@PostMapping("/{id}/status")
	public String updateStatus(@PathVariable Long id,
							   @RequestParam("status") AppointmentStatus status,
							   RedirectAttributes redirectAttributes) {
		try {
			appointmentService.updateAppointmentStatus(id, status);
			redirectAttributes.addFlashAttribute("successMessage", "Appointment #" + id + " status changed to " + status + ".");
		} catch (IllegalStateException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		return "redirect:/admin/appointments";
	}
}

package mn.icode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}

package mn.icode.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String listAppointments(@RequestParam(value = "status", required = false) AppointmentStatus selectedStatus,
                                   Authentication authentication,
                                   Model model) {
        if (selectedStatus != null) {
            try {
                model.addAttribute("appointments", appointmentService.getAppointmentsByStatus(selectedStatus));
            } catch (Exception e) {
                model.addAttribute("appointments", appointmentService.getAllAppointments());
            }
        } else {
            model.addAttribute("appointments", appointmentService.getAllAppointments());
        }

        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("selectedStatus", selectedStatus);

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("username", authentication.getName());
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        }

        return "admin/appointments/index";
    }
}
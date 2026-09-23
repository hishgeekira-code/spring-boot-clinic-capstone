package mn.icode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @PostMapping("/update-status")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestParam("appointmentId") Long appointmentId,
                                          @RequestParam("status") String status) {
        try {
            AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.trim().toUpperCase());
            appointmentService.updateAppointmentStatus(appointmentId, appointmentStatus);
            // Дахин хуудас татахгүй, 200 OK буцаана!
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
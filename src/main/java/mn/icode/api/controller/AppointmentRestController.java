package mn.icode.api.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mn.icode.model.Appointment;
import mn.icode.model.AppointmentStatus;
import mn.icode.model.User;
import mn.icode.repository.UserRepository;
import mn.icode.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    public AppointmentRestController(AppointmentService appointmentService, UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllAppointments() {
    	return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable Long id,
    												 @RequestBody Map<String, String> payload) {
    	String statusStr = payload.get("status");
    	if (statusStr == null) {
    		return ResponseEntity.badRequest().body(Map.of("error", "Status field is required."));
    	}

    	try {
    		AppointmentStatus status = AppointmentStatus.valueOf(statusStr.toUpperCase());
    		Appointment updated = appointmentService.updateAppointmentStatus(id, status);
    		return ResponseEntity.ok(Map.of(
    				"message", "Appointment status updated successfully.",
    				"id", updated.getId(),
    				"status", updated.getStatus()
    		));
    	} catch (IllegalArgumentException e) {
    		return ResponseEntity.badRequest().body(Map.of("error", "Invalid appointment status value: " + statusStr));
    	} catch (IllegalStateException e) {
    		return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    	}
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            appointmentService.cancelAppointment(id, user.getId());
            return ResponseEntity.ok(Map.of("message", "Appointment cancelled successfully."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
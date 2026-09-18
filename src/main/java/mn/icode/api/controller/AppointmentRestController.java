package mn.icode.api.controller;

import mn.icode.model.User;
import mn.icode.repository.UserRepository;
import mn.icode.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    public AppointmentRestController(AppointmentService appointmentService, UserRepository userRepository) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
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